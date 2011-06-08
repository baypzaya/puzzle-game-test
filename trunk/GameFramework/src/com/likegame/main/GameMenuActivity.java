package com.likegame.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mstanford.gameframe.R;
import com.mstanford.gameframework.GameActivity;

public class GameMenuActivity extends Activity implements OnClickListener{
	private Button startButton;
	private Button continueButton;
	private Button optionButton;
	private Button helpButton;
	private Button aboutButton;
	
	private boolean enableContinue=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		this.setContentView(R.layout.game_menu);
		startButton = (Button) findViewById(R.id.start);
		continueButton = (Button) findViewById(R.id.continue_start);
		optionButton = (Button) findViewById(R.id.option);
		helpButton = (Button) findViewById(R.id.help);
		aboutButton = (Button) findViewById(R.id.about);
		
		startButton.setOnClickListener(this);
		continueButton.setOnClickListener(this);
		optionButton.setOnClickListener(this);
		helpButton.setOnClickListener(this);
		aboutButton.setOnClickListener(this);
		
		
		if(!enableContinue){
			continueButton.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		switch (id){
		case R.id.start:
			Intent intent = new Intent(this,GameActivity.class);
			startActivity(intent);
			break;
		case R.id.continue_start:
			break;
		case R.id.option:
			break;
		case R.id.help:
			break;
		case R.id.about:
			break;
		}
		
	}
	
}
