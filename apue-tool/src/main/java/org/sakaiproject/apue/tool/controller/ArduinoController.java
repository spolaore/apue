package org.sakaiproject.apue.tool.controller;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.sakaiproject.apue.tool.Dado;
import org.sakaiproject.apue.tool.Metadado;
import org.sakaiproject.apue.tool.Sessao;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class ArduinoController implements SerialPortEventListener {
	
	SerialPort serialPort;
	
	private static final String PORT_NAME = "/dev/tty.usbmodemfd121";
	
	private BufferedReader input;
	private OutputStream output;
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	
	private volatile String lastValueRead;
	
	public void initialize() {
		CommPortIdentifier portId = null;
		
		@SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = portEnum.nextElement();
			if (currPortId.getName().equals(PORT_NAME)) {
				portId = currPortId;
				break;
			}
		}

		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection<String> getPorts() {
		Collection<String> ports = new HashSet<String>();
		Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = portEnum.nextElement();
			ports.add(currPortId.getName());
		}
		return ports;
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				Integer idSessao = Sessao.findSessaoByPortaExperimento(PORT_NAME);
				Sessao sessao = new Sessao();
				sessao.setIdSessao(idSessao);
				TypeReference<HashMap<String,String>> typeRef = new TypeReference<HashMap<String,String>>() {}; 
				ObjectMapper mapper = new ObjectMapper();
				HashMap<String,String> map = mapper.readValue(input.readLine(), typeRef); 
				for (Entry<String, String> entry : map.entrySet()) {
					Metadado metadado = new Metadado();
					Dado dado = new Dado();
					dado.setIdMetadado(metadado);
					metadado.setIdMetadado(Metadado.findIdMetadadoByChave(entry.getKey()));
					dado.setValor(entry.getValue());
					dado.setTipoDado(Dado.TipoDado.SAIDA.getValue());
					dado.setIdSessao(sessao);
					dado.persist();
				}
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	public synchronized void sendValue(int value) {
		try {
			output.write(value);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getLastValueRead() {
		return this.lastValueRead;
	}

	public void sendDados(ArrayList<Dado> dados) {
		final JsonNodeFactory json = JsonNodeFactory.instance;
		ObjectNode objectNode = json.objectNode();
		for (Dado dado : dados) {
			objectNode.put(dado.getIdMetadado().getChave(), dado.getValor());
		}
		
		try {
			output.write(objectNode.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}