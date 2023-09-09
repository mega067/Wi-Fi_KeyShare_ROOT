package com.WIFI.keyshare;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class redes_g extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redes_g);

        // Encuentra el ScrollView y el LinearLayout en tu layout
        ScrollView scrollView = findViewById(R.id.scrol_red_g);
        LinearLayout redesContainer = findViewById(R.id.redes_container);

        // Ejecuta el comando shell para obtener el contenido de WifiConfigStore.xml
        String wifiConfigStoreContent = obtenerWifiConfigStoreContent();

        // Procesa el contenido para extraer las redes y sus datos
        List<RedWiFi> redesWiFi = obtenerRedesDesdeWifiConfigStore(wifiConfigStoreContent);

        // Recorre la lista de objetos RedWiFi y crea TextViews para mostrar la información
        for (RedWiFi redWiFi : redesWiFi) {
            TextView textView = new TextView(this);
            textView.setText("SSID: " + redWiFi.getSsid() + "\nContraseña: " + redWiFi.getPsk() + "\n");
            redesContainer.addView(textView);
        }
    }

    private String obtenerWifiConfigStoreContent() {
        try {
            Process process = Runtime.getRuntime().exec("su"); // Obtener acceso root
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            // Ejecutar el comando shell para obtener el contenido de WifiConfigStore.xml
            Process catProcess = Runtime.getRuntime().exec("cat /data/misc/wifi/WifiConfigStore.xml");
            BufferedReader catReader = new BufferedReader(new InputStreamReader(catProcess.getInputStream()));

            String line;
            StringBuilder xmlContent = new StringBuilder();

            while ((line = catReader.readLine()) != null) {
                xmlContent.append(line).append("\n");
            }

            catReader.close();
            catProcess.waitFor();
            reader.close();
            process.waitFor();

            return xmlContent.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<RedWiFi> obtenerRedesDesdeWifiConfigStore(String xmlContent) {
        List<RedWiFi> redesWiFi = new ArrayList<>();

        if (xmlContent != null) {
            Pattern pattern = Pattern.compile("<WifiConfiguration>(.*?)</WifiConfiguration>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(xmlContent);

            while (matcher.find()) {
                String networkXml = matcher.group(1);

                Pattern ssidPattern = Pattern.compile("<string name=\"SSID\">\"(.*?)\"</string>");
                Matcher ssidMatcher = ssidPattern.matcher(networkXml);

                Pattern pskPattern = Pattern.compile("<string name=\"PreSharedKey\">\"(.*?)\"</string>");
                Matcher pskMatcher = pskPattern.matcher(networkXml);

                if (ssidMatcher.find() && pskMatcher.find()) {
                    String ssid = ssidMatcher.group(1);
                    String psk = pskMatcher.group(1);
                    redesWiFi.add(new RedWiFi(ssid, psk));
                }
            }
        }

        return redesWiFi;
    }
}
