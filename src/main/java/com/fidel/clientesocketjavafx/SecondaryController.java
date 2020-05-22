package com.fidel.clientesocketjavafx;

import datapaquete.DataPaquete;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SecondaryController {

    private final String IP_SERVIDOR = "192.168.0.28";
    private final int PUERTO_SERVIDOR = 40000;
    private final int PUERTO_CLIENTE = 50000;

    @FXML
    private Button secondaryButton;

    @FXML
    private Button enviarButton;

    @FXML
    private TextField mensaje;

    @FXML
    private TextArea areaMensaje;

    public SecondaryController() {
        recibeData();
    }

    @FXML
    void enviarMensaje(ActionEvent event) {
        System.out.println("Enviar mensaje");
        var usuario = "Bob";
        var ip = "192.168.0.28";
        var mensaje = this.mensaje.getText();
        DataPaquete data = new DataPaquete(usuario, ip, mensaje);

        try {
            Socket socket = new Socket(IP_SERVIDOR, PUERTO_SERVIDOR);
            ObjectOutputStream flujoSalida = new ObjectOutputStream(socket.getOutputStream());
            flujoSalida.writeObject(data);

            flujoSalida.close();

            socket.close();

        } catch (Exception e) {
            System.out.println("No se pudo conectar al servidor: " + IP_SERVIDOR);
        }
    }

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    private void recibeData() {
        var t = new Thread() {
            @Override
            public void run() {
                try {
                    DataPaquete data;
                    var serverCliente = new ServerSocket(PUERTO_CLIENTE);
                    Socket socket;

                    while (true) {
                        System.out.println("Cliente en escucha...");
                        socket = serverCliente.accept();

                        // flujo de entrada
                        var flujoEntrada = new ObjectInputStream(socket.getInputStream());
                        data = (DataPaquete) flujoEntrada.readObject();

                        var usuario = data.getNombreUsuario();
                        var ip = data.getDireccionIP();
                        var msg = data.getMensaje();

                        var msgConcat = usuario + "para: " + ip + ":"
                                + "\n" + msg + "\n";

                        System.out.println(msgConcat);

                        Platform.runLater(() -> {
                            areaMensaje.appendText(msgConcat);
                            // otras partes de la GUI
                        });

                    }

                } catch (IOException iOException) {
                    System.out.println("No se puede crear el socket en el cliente");
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

            }

        };
        t.start();
    }
}
