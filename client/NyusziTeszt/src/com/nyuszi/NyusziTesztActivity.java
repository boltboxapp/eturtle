package com.nyuszi;

import java.io.UnsupportedEncodingException;

import com.rabbitmq.client.AMQP.Channel;
import com.rabbitmq.client.AMQP.Connection;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class NyusziTesztActivity extends Activity {
	private MessageConsumer mConsumer;
	private TextView mOutput;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //The output TextView we'll use to display messages
        mOutput =  (TextView) findViewById(R.id.output);

        //Create the consumer
        mConsumer = new MessageConsumer("192.168.1.104",
        		"logs",
        		"fanout");

        //Connect to broker
        mConsumer.connectToRabbitMQ();

        //register for messages
        mConsumer.setOnReceiveMessageHandler(new MessageConsumer.OnReceiveMessageHandler(){

			public void onReceiveMessage(byte[] message) {
				String text = "";
				try {
					text = new String(message, "UTF8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				mOutput.append("\n"+text);
			}
        });
    }
    
    @Override
	protected void onResume() {
		super.onPause();
		mConsumer.connectToRabbitMQ();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mConsumer.dispose();
	}
    

}