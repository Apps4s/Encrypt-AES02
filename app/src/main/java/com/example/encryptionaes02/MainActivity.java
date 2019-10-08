package com.example.encryptionaes02;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {


  private EditText etTexto, etPassword;
  private TextView tvTexto;
  private Button btEncriptar, btDesEncriptar, btApiEncriptada;
  private String textoSalida;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    etTexto = findViewById(R.id.mainActivityEtTexto);
    etPassword = findViewById(R.id.mainActivityEtPassword);
    tvTexto = findViewById(R.id.mainActivityTvTexto);

    // Encriptar
    btEncriptar = findViewById(R.id.mainActivityBtEncriptar);
    btEncriptar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        try {
          textoSalida = encriptar(etTexto.getText().toString(), etPassword.getText().toString());
          tvTexto.setText(textoSalida);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    // Desencriptar
    btDesEncriptar = findViewById(R.id.mainActivityBtDesencritar);
    btDesEncriptar.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        try {
          textoSalida = desencriptar(textoSalida, etPassword.getText().toString());
          tvTexto.setText(textoSalida);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

  }

  private String desencriptar(String datos, String password) throws Exception {
    SecretKeySpec secretKeyc = generateKey(password);
    Cipher cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.DECRYPT_MODE, secretKeyc);
    byte[] datosDecodificados = Base64.decode(datos, Base64.DEFAULT);
    byte[] datosDesencriptadosByte = cipher.doFinal(datosDecodificados);
    String datosDesencriptadosString = new String(datosDesencriptadosByte);
    return datosDesencriptadosString;

  }

  private String encriptar(String datos, String password) throws Exception {
    SecretKeySpec secretKey = generateKey(password);
    Cipher cipher = Cipher.getInstance("AES");

    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    byte[] datosEncriptadosBytes = cipher.doFinal(datos.getBytes());
    String datosEncriptadosString = Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
    return datosEncriptadosString;
  }

  private SecretKeySpec generateKey(String password) throws Exception {
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    byte[] key = password.getBytes("UTF-8");

    sha.update(key, 0, key.length);
    key = sha.digest();
//    key = sha.digest(key);

    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    return secretKeySpec;
  }

}
