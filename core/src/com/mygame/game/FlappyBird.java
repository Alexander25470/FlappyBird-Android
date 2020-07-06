package com.mygame.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import javax.xml.soap.Text;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture gameOver;

	Circle birdCircle;
	Rectangle[] topRectangle,bottomRectangle;
	ShapeRenderer shapeRenderer;

	int birdState=0;
	float birdY=0;
	float velocity=0;
	int gameState=0;
	int score=0;
	int scoringTube=0;
	float gravity= (float) 2.0;
	Texture topTube,bottomTube;
	float gap=600;
	float maxTubeOffset;
	Random random;
	float[] tubeOffset;
	float tubeVelocity=4;
	float[] tubeX;
	int numberOfTubes=4;
	float distanceBetweenTubes;
	BitmapFont font;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];

		birdCircle = new Circle();
		shapeRenderer = new ShapeRenderer();

		birds[0]=new Texture("bird.png");
		birds[1]=new Texture("bird2.png");

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		random = new Random();
		distanceBetweenTubes=Gdx.graphics.getWidth()/2;
		tubeX = new float[numberOfTubes];
		tubeOffset=new float[numberOfTubes];
		topRectangle = new Rectangle[numberOfTubes];
		bottomRectangle = new Rectangle[numberOfTubes];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		gameOver = new Texture ("gameover.png");

		gamestart();

	}
	public void gamestart(){
		birdY = Gdx.graphics.getHeight()/2-birds[0].getHeight()/2;
		for (int i=0;i<numberOfTubes;i++){
			tubeX[i]=Gdx.graphics.getWidth()/2-topTube.getWidth()/2 + Gdx.graphics.getWidth() + (i*distanceBetweenTubes);
			topRectangle[i]=new Rectangle();
			bottomRectangle[i]=new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.end();



		if (gameState==1){

			if(tubeX[scoringTube]<Gdx.graphics.getWidth()/2){
				score++;
				if (scoringTube<numberOfTubes-1){
					scoringTube++;

				}
				else{
					scoringTube=0;
				}
			}

			if (Gdx.input.justTouched()){

				velocity=-30;

				//Gdx.app.log("tap","touched");
				//gameState=1;

			}



			for (int i=0;i<numberOfTubes;i++){
				if (tubeX[i]< -topTube.getWidth()){
					tubeOffset[i] = (random.nextFloat()- 0.5f)*(Gdx.graphics.getHeight()-gap-200);
					tubeX[i]=tubeX[i]+(numberOfTubes*distanceBetweenTubes);
				}else {
					tubeX[i]=tubeX[i]-tubeVelocity;


				}

				batch.begin();

				batch.draw(topTube,tubeX[i],Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i]);
				batch.draw(bottomTube,tubeX[i],Gdx.graphics.getHeight()/2 - (gap/2 + bottomTube.getHeight() - tubeOffset[i]));

				topRectangle[i]=new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
				bottomRectangle[i]= new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2 - (gap/2 + bottomTube.getHeight() - tubeOffset[i]),bottomTube.getWidth(),bottomTube.getHeight());



				batch.end();
			}





			if (birdY>0){
				velocity=velocity+gravity;
				birdY-=velocity;
			}
			if(birdY<=0){
				gameState=2;
			}
			if (birdY>Gdx.graphics.getHeight()-birds[0].getHeight()/2){
				birdY=Gdx.graphics.getHeight()-birds[0].getHeight()/2;
			}

		}else if (gameState==0){

			if (Gdx.input.justTouched()){
				gameState=1;
			}

		}
		if (gameState==2)
			{
				batch.begin();
				batch.draw(gameOver,Gdx.graphics.getWidth()/2-gameOver.getWidth()/2,Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);
				batch.end();

				if (Gdx.input.justTouched()){
					gamestart();

					scoringTube=0;
					score=0;
					velocity=0;
					gameState=1;
				}

			}

		if(birdState==0){
			birdState=1;
		}
		else{
			birdState=0;
		}


		batch.begin();
		batch.draw(birds[birdState],Gdx.graphics.getWidth()/2-birds[birdState].getWidth()/2,birdY);


		//font.draw(batch,String.valueOf(score),100,200);
		//font.draw(batch,"Score: ",0,200);
		font.draw(batch,String.valueOf(score),(font.draw(batch,"Score: ",0,200)).width,200);


		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[birdState].getHeight()/2,birds[birdState].getWidth()/2);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.BLACK);

		//shapeRenderer.circle(Gdx.graphics.getWidth()/2,birdY+birds[birdState].getHeight()/2,birds[birdState].getWidth()/2);

		for (int i=0; i<numberOfTubes;i++)
		{
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2 + gap/2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2 - (gap/2 + bottomTube.getHeight() - tubeOffset[i]),bottomTube.getWidth(),bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle,topRectangle[i])||Intersector.overlaps(birdCircle,bottomRectangle[i])){
				Gdx.app.log("collision","its ok");
				gameState=2;
			}
		}

		//shapeRenderer.end();



	}
	
	@Override
	public void dispose () {

	}
}
