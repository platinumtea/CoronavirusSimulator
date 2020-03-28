import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class CoronaPanel extends JPanel {
	private final int DELAY = 10, PEOPLE = 300, RECORD_RATE = 2, IMMOBILE = 200, CAPACITY = 50;
	private int graphScale = 2;
	static final int WIDTH = 600, HEIGHT = 800;
	Timer timer;
	private int tick, timerTriggers;

	Person[] people, peopleDistanced;
	int[][] graph, graphDistanced;
	
	private JSlider scaleSlider;

	public CoronaPanel() {
		timerTriggers = 0;
		tick = 0;
		people = new Person[PEOPLE];
		peopleDistanced = new Person[PEOPLE];
		graph = new int[4][WIDTH];
		graphDistanced = new int[4][WIDTH];

		int infected = (int) (Math.random() * PEOPLE);
		for (int i = 0; i < people.length; i++) {
			people[i] = new Person((int) (Math.random() * (WIDTH - 10)), (int) (Math.random() * (HEIGHT - 10)),
					i == infected ? true : false, false, false);
		}

		infected = (int) (Math.random() * (PEOPLE - IMMOBILE)) + IMMOBILE;
		for (int i = 0; i < people.length; i++) {
			if (i < IMMOBILE) {
				peopleDistanced[i] = new Person((int) (Math.random() * (WIDTH - 10)) + WIDTH,
						(int) (Math.random() * (HEIGHT - 10)), false, true, true);
			} else {
				peopleDistanced[i] = new Person((int) (Math.random() * (WIDTH - 10)) + WIDTH,
						(int) (Math.random() * (HEIGHT - 10)), i == infected ? true : false, false, true);
			}
		}

		scaleSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 2);
		scaleSlider.setMajorTickSpacing(2); 
		scaleSlider.setMinorTickSpacing(1);
		scaleSlider.setPaintTicks(true);
		scaleSlider.setPaintTicks(true);
		scaleSlider.setPaintLabels(true);
		scaleSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		SliderListener sliderListener = new SliderListener();
		scaleSlider.addChangeListener(sliderListener);
		
		add(scaleSlider);
			
		
		timer = new Timer(DELAY, new CoronaListener());
		setPreferredSize(new Dimension(WIDTH * 2, HEIGHT + (PEOPLE / graphScale)));
		setBackground(Color.white);
		timer.start();
	}

	public void paintComponent(Graphics page) {
		super.paintComponent(page);

		// Draw people (normal)
		for (int i = 0; i < people.length; i++) {
			switch (people[i].getStatus()) {
			case 0:
				page.setColor(Color.green);
				break;
			case 1:
				page.setColor(Color.red);
				break;
			case 2:
				page.setColor(Color.pink);
				break;
			case 3:
				page.setColor(Color.blue);
				break;
			case 4:
				page.setColor(Color.gray);
				break;
			}
			if (timerTriggers % RECORD_RATE == 0 && people[i].getStatus() != 4) {
				graph[people[i].getStatus()][tick]++;
			}
			page.fillOval((int) people[i].getX(), (int) people[i].getY(), 10, 10);
			for (int j = 0; j < people.length; j++) {
				if (i != j) {
					people[i].check(people[j]);
				}
			}
		}

		// Draw people (distanced)
		for (int i = 0; i < peopleDistanced.length; i++) {
			switch (peopleDistanced[i].getStatus()) {
			case 0:
				page.setColor(Color.green);
				break;
			case 1:
				page.setColor(Color.red);
				break;
			case 2:
				page.setColor(Color.pink);
				break;
			case 3:
				page.setColor(Color.blue);
				break;
			case 4:
				page.setColor(Color.gray);
				break;
			}
			if (timerTriggers % RECORD_RATE == 0 && peopleDistanced[i].getStatus() != 4) {
				graphDistanced[peopleDistanced[i].getStatus()][tick]++;
			}
			page.fillOval((int) peopleDistanced[i].getX(), (int) peopleDistanced[i].getY(), 10, 10);
			for (int j = 0; j < peopleDistanced.length; j++) {
				if (i != j) {
					peopleDistanced[i].check(peopleDistanced[j]);
				}
			}
		}

		// Draw graph (normal)
		for (int i = 0; i < tick; i++) {
			int temp = HEIGHT + (PEOPLE / graphScale);

			page.setColor(Color.pink);
			page.drawLine(i, temp - ((int) Math.ceil(graph[2][i] / graphScale)), i, temp);
			temp -= Math.ceil(graph[2][i] / graphScale);

			page.setColor(Color.red);
			page.drawLine(i, temp - ((int) Math.ceil(graph[1][i] / graphScale)), i, temp);
			temp -= Math.ceil(graph[1][i] / graphScale);

			page.setColor(Color.green);
			page.drawLine(i, temp - ((int) Math.ceil(graph[0][i] / graphScale)), i, temp);
			temp -= Math.ceil(graph[0][i] / graphScale);

			page.setColor(Color.blue);
			page.drawLine(i, temp - ((int) Math.ceil(graph[3][i] / graphScale)), i, temp);
			temp -= Math.ceil(graph[3][i] / graphScale);

			page.setColor(Color.gray);
			page.drawLine(i, HEIGHT, i, temp);
		}
		page.setColor(Color.black);
		page.drawLine(0, HEIGHT + ((PEOPLE - CAPACITY) / graphScale), WIDTH,
				HEIGHT + ((PEOPLE - CAPACITY) / graphScale));

		// Draw graph (distanced)
		for (int i = WIDTH + 1; i < WIDTH + tick; i++) {
			int temp = HEIGHT + (PEOPLE / graphScale);

			page.setColor(Color.pink);
			page.drawLine(i, temp - ((int) Math.ceil(graphDistanced[2][i - WIDTH] / graphScale)), i, temp);
			temp -= Math.ceil((graphDistanced[2][i - WIDTH] / graphScale));

			page.setColor(Color.red);
			page.drawLine(i, temp - ((int) Math.ceil(graphDistanced[1][i - WIDTH] / graphScale)), i, temp);
			temp -= Math.ceil((graphDistanced[1][i - WIDTH] / graphScale));

			page.setColor(Color.green);
			page.drawLine(i, temp - ((int) Math.ceil(graphDistanced[0][i - WIDTH] / graphScale)), i, temp);
			temp -= Math.ceil((graphDistanced[0][i - WIDTH] / graphScale));

			page.setColor(Color.blue);
			page.drawLine(i, temp - ((int) Math.ceil(graphDistanced[3][i - WIDTH] / graphScale)), i, temp);
			temp -= Math.ceil((graphDistanced[3][i - WIDTH] / graphScale));

			page.setColor(Color.gray);
			page.drawLine(i, HEIGHT, i, temp);
		}
		page.setColor(Color.black);
		page.drawLine(WIDTH, HEIGHT + ((PEOPLE - CAPACITY) / graphScale), WIDTH * 2,
				HEIGHT + ((PEOPLE - CAPACITY) / graphScale));
		
		page.drawLine(WIDTH, 0, WIDTH, HEIGHT + (PEOPLE / graphScale));
	}
	private class SliderListener implements ChangeListener {
		
		public void stateChanged(ChangeEvent e) {
			graphScale = scaleSlider.getValue();
			Corona.frame.setSize(new Dimension(WIDTH * 2 + 16, HEIGHT + (PEOPLE / graphScale) + 39));
		}
	}
	
	private class CoronaListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < people.length; i++) {
				people[i].tick();
				if (people[i].isInfected() && i < PEOPLE * 0.2 && graph[2][i] < CAPACITY) {
					people[i].hospitalize();
				} else if (people[i].isInfected() && i < PEOPLE * 0.2) {
					people[i].kill();
				}
				peopleDistanced[i].tick();
				if (peopleDistanced[i].isInfected() && i < PEOPLE * 0.2 && graphDistanced[2][i] < CAPACITY) {
					peopleDistanced[i].hospitalize();
				} else if (peopleDistanced[i].isInfected() && i < PEOPLE * 0.2) {
					peopleDistanced[i].kill();
				}
			}
			repaint();
			timerTriggers++;
			if (timerTriggers % RECORD_RATE == 0) {
				tick++;
			}
			if (tick > 599) {
				tick = 0;
				graph = new int[4][WIDTH];
				graphDistanced = new int[4][WIDTH];
			}
		}
	}
}
