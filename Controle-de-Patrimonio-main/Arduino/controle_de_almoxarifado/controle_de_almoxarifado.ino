#include <WiFi.h>
#include <HTTPClient.h>
#include <SPI.h>
#include <MFRC522.h>

// =========================
// CONFIGURAÇÃO DO RFID
// =========================

#define SS_PIN 5
#define RST_PIN 22

MFRC522 rfid(SS_PIN, RST_PIN);

// =========================
// CONFIGURAÇÃO DO WIFI
// =========================

const char* WIFI_SSID = "Controle de Patrimonio";
const char* WIFI_PASSWORD = "Senha123";

// =========================
// CONFIGURAÇÃO DO BACKEND
// =========================

// IMPORTANTE:
// Não use localhost aqui.
//
// O ESP32 precisa acessar o IP do computador
// onde o Spring Boot está executando.
//
// Exemplo:
// http://192.168.0.100:8080/api/rfid/leitura

const char* SERVER_URL =
    "http://192.168.0.100:8080/api/rfid/leitura";

// Identificação deste ESP32
const char* DEVICE_ID = "ESP32_SENAI_01";

// =========================
// CONTROLE DE LEITURA
// =========================

String ultimoUID = "";

unsigned long ultimaLeitura = 0;

const unsigned long INTERVALO_LEITURA = 3000;


// =========================
// SETUP
// =========================

void setup() {

    Serial.begin(115200);

    delay(1000);

    Serial.println();
    Serial.println("==============================");
    Serial.println(" SISTEMA RFID - SENAI");
    Serial.println("==============================");

    // Inicializa SPI
    SPI.begin();

    // Inicializa MFRC522
    rfid.PCD_Init();

    Serial.println("MFRC522 inicializado.");

    // Conecta no Wi-Fi
    conectarWiFi();
}


// =========================
// LOOP
// =========================

void loop() {

    // Se perdeu o Wi-Fi, tenta reconectar
    if (WiFi.status() != WL_CONNECTED) {

        Serial.println("Wi-Fi desconectado.");

        conectarWiFi();
    }

    // Verifica se existe uma nova tag
    if (!rfid.PICC_IsNewCardPresent()) {
        return;
    }

    // Tenta ler a tag
    if (!rfid.PICC_ReadCardSerial()) {
        return;
    }

    // Monta o UID
    String uid = obterUID();

    Serial.println();
    Serial.println("------------------------------");
    Serial.println("TAG RFID DETECTADA");
    Serial.print("UID: ");
    Serial.println(uid);

    // Evita enviar a mesma tag várias vezes
    if (uid == ultimoUID &&
        millis() - ultimaLeitura < INTERVALO_LEITURA) {

        Serial.println("Leitura ignorada: mesma tag.");

        rfid.PICC_HaltA();
        rfid.PCD_StopCrypto1();

        return;
    }

    ultimoUID = uid;
    ultimaLeitura = millis();

    // Envia para o backend
    enviarParaBackend(uid);

    // Finaliza comunicação com a tag
    rfid.PICC_HaltA();
    rfid.PCD_StopCrypto1();

    delay(500);
}


// =========================
// CONECTAR WIFI
// =========================

void conectarWiFi() {

    Serial.print("Conectando ao Wi-Fi");

    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

    int tentativas = 0;

    while (WiFi.status() != WL_CONNECTED &&
           tentativas < 30) {

        delay(500);

        Serial.print(".");

        tentativas++;
    }

    Serial.println();

    if (WiFi.status() == WL_CONNECTED) {

        Serial.println("Wi-Fi conectado!");

        Serial.print("IP do ESP32: ");
        Serial.println(WiFi.localIP());

    } else {

        Serial.println("Não foi possível conectar ao Wi-Fi.");
    }
}


// =========================
// OBTER UID
// =========================

String obterUID() {

    String uid = "";

    for (byte i = 0;
         i < rfid.uid.size;
         i++) {

        if (rfid.uid.uidByte[i] < 0x10) {
            uid += "0";
        }

        uid += String(
            rfid.uid.uidByte[i],
            HEX
        );
    }

    uid.toUpperCase();

    return uid;
}


// =========================
// ENVIAR PARA BACKEND
// =========================

void enviarParaBackend(String uid) {

    if (WiFi.status() != WL_CONNECTED) {

        Serial.println(
            "Wi-Fi não conectado. "
            "Leitura não enviada."
        );

        return;
    }

    HTTPClient http;

    Serial.println("Enviando UID para o backend...");

    http.begin(SERVER_URL);

    http.addHeader(
        "Content-Type",
        "application/json"
    );

    // Monta JSON
    String json = "{";

    json += "\"uid\":\"";
    json += uid;
    json += "\",";

    json += "\"deviceId\":\"";
    json += DEVICE_ID;
    json += "\"";

    json += "}";

    Serial.print("JSON enviado: ");
    Serial.println(json);

    // POST
    int httpResponseCode =
        http.POST(json);

    Serial.print("HTTP Status: ");
    Serial.println(httpResponseCode);

    if (httpResponseCode > 0) {

        String resposta =
            http.getString();

        Serial.println("Resposta do servidor:");

        Serial.println(resposta);

    } else {

        Serial.print(
            "Erro ao enviar HTTP: "
        );

        Serial.println(http.errorToString(
            httpResponseCode
        ));
    }

    http.end();
}