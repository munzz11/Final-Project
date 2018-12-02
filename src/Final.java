import java.util.Random;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Final extends Application {

	Random random = new Random();

	

	double Color1 = 200;
	

	private double gravityX, gravityY;

	private double[] x = new double[inputs.Particles];
	private double[] y = new double[inputs.Particles];
	private double[] velocityX = new double[inputs.Particles];
	private double[] velocityY = new double[inputs.Particles];
	private Color[] colors = new Color[(int) Color1];

	private PixelWriter pixelWriter;
	private GraphicsContext fx;

	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Final Project");
		gravityX = 1920/2;
		gravityY = 1080/2;

		for (int i = 0; i < inputs.Particles; i++) {
			x[i] = random.nextDouble() * 1920;
			y[i] = random.nextDouble() * 1080;
			velocityX[i] = 0;
			velocityY[i] = 0;
		}

		for (int i = 0; i < Color1; i++) {
			
			colors[i] = Color.hsb(inputs.hue, 1d, 1d);
		}

		Group root = new Group();
		Canvas canvas = new Canvas(1920, 1080);
		fx = canvas.getGraphicsContext2D();
		root.getChildren().add(canvas);
		Scene scene = new Scene(root, Color.BLACK);
		primaryStage.setScene(scene);
		primaryStage.show();

		pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();

		canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, new MouseHandler());
		canvas.addEventFilter(MouseEvent.MOUSE_CLICKED, new MouseHandler());

		AnimationTimer gameLoop = new MainGameLoop();
		gameLoop.start();

	}

	public double distanceFromCenter(double x, double y) {
		double distX = gravityX - x;
		double distY = gravityY - y;
		return Math.sqrt((distX * distX + distY * distY));
	}

	class MainGameLoop extends AnimationTimer {
		@Override
		public void handle(long now) {
			fx.setFill(Color.BLACK);
			fx.fillRect(0, 0, 1920, 1080);

			for (int i = 0; i < inputs.Particles; i++) {
				double x1 = x[i];
				double y1 = y[i];

				double distX = x1 - gravityX;
				double distY = y1 - gravityY;

				double dist = distanceFromCenter(x1, y1);
				double angle = Math.atan(distY / distX);

				double force = 2000 / (dist * dist);
				double forceX = force * Math.cos(angle);
				double forceY = force * Math.sin(angle);

				if (x1 < gravityX) {
					velocityX[i] += forceX;
					velocityY[i] += forceY;
				} else {
					velocityX[i] -= forceX;
					velocityY[i] -= forceY;
				}

				if (x[i] < 0) {
					x[i] = 0;
					velocityX[i] = 0;
				}
				if (x[i] > 1920) {
					x[i] = 1920;
					velocityX[i] = 0;
				}
				if (y[i] < 0) {
					y[i] = 0;
					velocityY[i] = 0;
				}
				if (y[i] > 1080) {
					y[i] = 1080;
					velocityY[i] = 0;
				}
				x[i] += velocityX[i];
				y[i] += velocityY[i];

				double hue = Color1;
				hue--;
			

				pixelWriter.setColor((int) x1, (int) y1, colors[(int) hue]);
			}
		}
	}

	class MouseHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			gravityX = event.getSceneX();
			gravityY = event.getSceneY();
		}
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		
		System.out.println("Enter value for Color");
		inputs.hue = input.nextInt();
		
		System.out.println("Enter max speed");
		inputs.MaxSpeed = input.nextInt();
		
		System.out.println("Enter number of particles desired");
		inputs.Particles = input.nextInt();
		
		
		launch(args);
		input.close();
	}
	
	public static class inputs {
		static int hue;
		static int MaxSpeed;
		static int Particles;
	}
}

