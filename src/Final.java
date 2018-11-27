import java.util.Random;

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

	static final int MaxSpeed = 5;

	double Color1 = 200;
	static final int Particles = 50000;

	private double gravityX, gravityY;

	private double[] x = new double[Particles];
	private double[] y = new double[Particles];
	private double[] velocityX = new double[Particles];
	private double[] velocityY = new double[Particles];
	private Color[] colors = new Color[(int) Color1];

	private PixelWriter pixelWriter;
	private GraphicsContext gfx;

	@Override
	public void start(final Stage primaryStage) {
		primaryStage.setTitle("Final Project");
		gravityX = 300;
		gravityY = 300;

		for (int i = 0; i < Particles; i++) {
			x[i] = random.nextDouble() * 1920;
			y[i] = random.nextDouble() * 1080;
			velocityX[i] = 0;
			velocityY[i] = 0;
		}

		for (int i = 0; i < Color1; i++) {
			double hue = 240d;
			colors[i] = Color.hsb(hue, 1d, 1d);
		}

		Group root = new Group();
		Canvas canvas = new Canvas(1920, 1080);
		gfx = canvas.getGraphicsContext2D();
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
			gfx.setFill(Color.BLACK);
			gfx.fillRect(0, 0, 1920, 1080);

			for (int i = 0; i < Particles; i++) {
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
					x[i] = 1080;
					velocityX[i] = 0;
				}
				if (y[i] < 0) {
					y[i] = 0;
					velocityY[i] = 0;
				}
				if (y[i] > 1920) {
					y[i] = 1080;
					velocityY[i] = 0;
				}
				x[i] += velocityX[i];
				y[i] += velocityY[i];

				// color logic
				double hue = Math.sqrt((velocityX[i] * velocityX[i] + velocityY[i] * velocityY[i]));
				hue /= MaxSpeed;
				if (hue > 1)
					hue = 1;
				hue *= Color1;
				hue--;
				if (hue < 0)
					hue = 0;

				pixelWriter.setColor((int) x1, (int) y1, colors[(int) hue]);
			}
		}
	}

	class MouseHandler implements EventHandler<MouseEvent> {
		@Override
		public void handle(MouseEvent event) {
			System.out.println("Mouse Clicked at: ( x: " + event.getSceneX() + ", y: " + event.getSceneY() + ")");
			gravityX = event.getSceneX();
			gravityY = event.getSceneY();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
//beep1234