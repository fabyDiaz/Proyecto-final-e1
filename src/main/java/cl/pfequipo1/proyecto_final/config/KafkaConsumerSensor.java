package cl.pfequipo1.proyecto_final.config;

import cl.pfequipo1.proyecto_final.dto.SensorDataDTO;
import cl.pfequipo1.proyecto_final.dto.SensorDataRequestDTO;
import cl.pfequipo1.proyecto_final.service.SensorDataServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Profile("kafka")
public class KafkaConsumerSensor implements CommandLineRunner {
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final CountDownLatch latch = new CountDownLatch(1);
    private KafkaConsumer<String, String> consumer;

    @Autowired
    private SensorDataServiceImpl sensorDataService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        // Configuración del consumidor
        String topicName = "Paralelo_01_Grupo_01";  // Nombre del canal
        String groupId = "sensor_data_consumer";  // Grupo de consumidores

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "lw01.ddns.net:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");

        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton(topicName));

        System.out.println("📡 Consumidor escuchando en el canal: " + topicName);

        // Agregar un hook de apagado para cerrar adecuadamente el consumidor
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("⚠️ Cerrando el consumidor...");
            close();
        }));

        // Iniciar el bucle de consumo en un hilo separado
        Thread consumerThread = new Thread(() -> {
            try {
                consume();
            } catch (Exception e) {
                System.err.println("Error en el consumidor: " + e.getMessage());
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });

        consumerThread.start();

        // Esperar a que el consumidor se cierre antes de salir
        latch.await();
    }

    private void consume() {
        try {
            while (!closed.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("📩 Mensaje recibido: " + record.value());
                    try {
                        processSensorData(record.value());
                    } catch (Exception e) {
                        System.err.println("Error al procesar el mensaje: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (WakeupException e) {
            if (!closed.get()) {
                throw e;
            }
            // Ignorar la excepción si el consumidor está siendo cerrado
        } finally {
            consumer.close();
            System.out.println("⚠️ Consumidor cerrado correctamente.");
        }
    }

    private void processSensorData(String jsonMessage) throws Exception {
        // Convertir el mensaje JSON a SensorDataRequestDTO
        SensorDataRequestDTO requestDTO = parseJsonMessage(jsonMessage);

        if (requestDTO != null) {
            System.out.println("Procesando datos del sensor con API key: " + requestDTO.getApi_key());
            System.out.println("Número de registros recibidos: " + requestDTO.getJson_data().size());

            // Procesar y guardar los datos usando el servicio
            List<SensorDataDTO> processedData = sensorDataService.processSensorDataRequest(requestDTO);
            System.out.println("✅ Datos guardados correctamente: " + processedData.size() + " registros");

            // Opcional: mostrar algunos detalles de los datos guardados
            if (!processedData.isEmpty()) {
                SensorDataDTO firstData = processedData.get(0);
                System.out.println("Ejemplo de datos - ID: " + firstData.getId() +
                        ", Sensor ID: " + firstData.getSensorId() +
                        ", Temperatura: " + firstData.getTemperature() +
                        ", Timestamp: " + firstData.getTimeStamp());
            }
        }
    }

    private SensorDataRequestDTO parseJsonMessage(String jsonMessage) {
        try {
            // Usar Jackson para convertir JSON a SensorDataRequestDTO
            return objectMapper.readValue(jsonMessage, SensorDataRequestDTO.class);
        } catch (Exception e) {
            System.err.println("Error al parsear el mensaje JSON: " + e.getMessage());

            // Intento alternativo en caso de error con Jackson
            try {
                SensorDataRequestDTO requestDTO = new SensorDataRequestDTO();
                JSONObject jsonObject = new JSONObject(jsonMessage);

                // Extraer API key
                String apiKey = jsonObject.getString("api_key");
                requestDTO.setApi_key(apiKey);

                // Extraer datos JSON
                JSONArray jsonDataArray = jsonObject.getJSONArray("json_data");
                List<Map<String, Object>> dataList = new java.util.ArrayList<>();

                for (int i = 0; i < jsonDataArray.length(); i++) {
                    JSONObject dataObj = jsonDataArray.getJSONObject(i);
                    Map<String, Object> dataMap = new java.util.HashMap<>();

                    // Extraer datetime/timestamp
                    if (dataObj.has("datetime")) {
                        dataMap.put("timestamp", dataObj.getLong("datetime"));
                    }

                    // Extraer temperatura
                    if (dataObj.has("temp")) {
                        dataMap.put("temperature", dataObj.getDouble("temp"));
                    }

                    // Extraer humedad (aunque la guardamos como voltage en este caso,
                    // ya que es el campo disponible en el DTO)
                    if (dataObj.has("humidity")) {
                        dataMap.put("voltage", dataObj.getDouble("humidity"));
                    }

                    dataList.add(dataMap);
                }

                requestDTO.setJson_data(dataList);
                return requestDTO;

            } catch (Exception ex) {
                System.err.println("Error en el parseo alternativo: " + ex.getMessage());
                return null;
            }
        }
    }

    /**
     * Cierra el consumidor de Kafka de manera segura
     */
    public void close() {
        closed.set(true);
        consumer.wakeup();
    }
}
