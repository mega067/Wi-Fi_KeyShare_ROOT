package com.WIFI.keyshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activity_home extends Activity {
    private Button buttonRedG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonRedG = findViewById(R.id.button_red_g);

        buttonRedG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Comando para verificar si el dispositivo está rooteado
                String commandToCheckRoot = "su -c 'whoami'";
                boolean isRooted = executeShellCommand(commandToCheckRoot);

                if (isRooted) {
                    // El dispositivo está rooteado, puedes proceder
                    Intent intent = new Intent(activity_home.this, redes_g.class);
                    startActivity(intent);
                } else {
                    // El dispositivo no está rooteado, muestra un mensaje o toma una acción alternativa
                    // Por ejemplo, puedes mostrar un Toast o un diálogo
                    // Toast.makeText(activity_home.this, "El dispositivo no está rooteado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para ejecutar un comando de shell y verificar el resultado sacado de internet XD
    private boolean executeShellCommand(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

