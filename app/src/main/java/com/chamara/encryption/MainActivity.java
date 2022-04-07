package com.chamara.encryption;

import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MaterialAutoCompleteTextView spinner;
    TextInputEditText txtInput,txtKey,txtOutput;
    Button btnEncrypt,btnDecrypt,btnReset;

    String ALGORITHM,TEXT_INPUT,ENCRYPTION_KEY,ENCRYPTED_TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.txtAlgorithm);
        btnEncrypt = findViewById(R.id.button);
        btnDecrypt = findViewById(R.id.button2);
        btnReset = findViewById(R.id.button3);
        txtInput = findViewById(R.id.txtInput);
        txtKey = findViewById(R.id.txtKey);
        txtOutput = findViewById(R.id.txtOutput);


        ArrayList items = new ArrayList();
        items.add("AES");
        items.add("Design");
        items.add("Components");
        items.add("Android");

        ArrayAdapter adapter;
        adapter = new ArrayAdapter(this,R.layout.list_item,items);

        spinner.setAdapter(adapter);

        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInputData();
                if (checkInputData() == true){
                    if (ALGORITHM.equals("AES")) {
                        try {
                            encryptAES();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Plese select AES for now", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInputData();
                if (checkInputData() == true){
                    if (ALGORITHM.equals("AES")) {
                        try {
                            decryptAES();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Plese select AES for now", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtInput.setText("");
                txtKey.setText("");
                txtOutput.setText("");
            }
        });
    }

    private void decryptAES() throws Exception {
        Toast.makeText(this, "Decrypting AES...", Toast.LENGTH_SHORT).show();
        SecretKeySpec key = generateKey(ENCRYPTION_KEY);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedVal = Base64.decode(ENCRYPTED_TEXT,Base64.DEFAULT);
        byte[] decVal = c.doFinal(decodedVal);
        String decValStr = new String(decVal);
        txtOutput.setText(decValStr);
    }

    private void encryptAES() throws Exception {
        Toast.makeText(this, "Encrypting AES...", Toast.LENGTH_SHORT).show();
        SecretKeySpec key = generateKey(ENCRYPTION_KEY);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(TEXT_INPUT.getBytes(StandardCharsets.UTF_8));
        String encryptedVal = Base64.encodeToString(encVal,Base64.DEFAULT);
        txtOutput.setText(encryptedVal);
    }
    private SecretKeySpec generateKey(String pass) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = pass.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }

    private boolean checkInputData() {
        if (ALGORITHM.length() > 1 && TEXT_INPUT.length() > 1 && ENCRYPTION_KEY.length() > 1 ) {
            return true;    //checking all required fields filled.
        } else
            Toast.makeText(this, "Missing Required Field!", Toast.LENGTH_SHORT).show();
            return false;
    }

    private void getInputData() {
        ALGORITHM = spinner.getText().toString();
        TEXT_INPUT = txtInput.getText().toString();
        ENCRYPTION_KEY = txtKey.getText().toString();
        ENCRYPTED_TEXT = txtOutput.getText().toString();
    }
}