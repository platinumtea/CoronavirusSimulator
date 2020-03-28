import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class CoronaPanel extends JPanel {
	private final int DELAY = 100, PEOPLE = 300, RECORD_RATE = 2, IMMOBILE = 200, CAPACITY = 50, LAYOUT_WIDTH = 200;
	private int graphScale = 2, numPeople = PEOPLE;
	static final int WIDTH = 600, HEIGHT = 800;
	Timer timer;
	private int tick, timerTriggers;
	private boolean paused = false;

	Person[] people, peopleDistanced;
	int[][] graph, graphDistanced;

	private JPanel controls;
	private JSlider scaleSlider, timerSlider, peopleSlider;
	private JLabel controlLabel, scaleLabel, timerLabel, peopleLabel;
	private JButton pauseButton, restartButton, playButton;

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
		SliderListener sliderListener = new SliderListener();

		controls = new JPanel();
		BoxLayout sliderLayout = new BoxLayout(controls, BoxLayout.Y_AXIS);
		controls.setLayout(sliderLayout);

		scaleSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, 2);
		scaleSlider.setMajorTickSpacing(1);
		scaleSlider.setMinorTickSpacing(1);
		scaleSlider.setPaintTicks(true);
		scaleSlider.setPaintLabels(true);
		scaleSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		scaleSlider.addChangeListener(sliderListener);
		scaleSlider.setEnabled(false);

		scaleLabel = new JLabel("Graph Scaling: 2");
		scaleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		timerSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, DELAY);
		timerSlider.setMajorTickSpacing(10);
		timerSlider.setMinorTickSpacing(5);
		timerSlider.setPaintTicks(true);
		timerSlider.setPaintLabels(true);
		timerSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		timerSlider.addChangeListener(sliderListener);

		timerLabel = new JLabel("Tickrate: " + DELAY);
		timerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		peopleSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, PEOPLE);
		peopleSlider.setMajorTickSpacing(250);
		peopleSlider.setMinorTickSpacing(50);
		peopleSlider.setPaintTicks(true);
		peopleSlider.setPaintLabels(true);
		peopleSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
		peopleSlider.addChangeListener(sliderListener);
		peopleSlider.setEnabled(false);

		peopleLabel = new JLabel("People: " + PEOPLE);
		peopleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

		controlLabel = new JLabel("Controls");
		controlLabel.setFont(new Font("Consolas", Font.BOLD, 40));

		pauseButton = new JButton("Pause");
		pauseButton.setContentAreaFilled(false);
		pauseButton.setOpaque(true);
		pauseButton.addActionListener(new ControlListener());

		playButton = new JButton("Play");
		playButton.setContentAreaFilled(false);
		playButton.setOpaque(true);
		playButton.addActionListener(new ControlListener());

		restartButton = new JButton("Restart");
		restartButton.setContentAreaFilled(false);
		restartButton.setOpaque(true);
		restartButton.addActionListener(new ControlListener());

		controls.add(controlLabel);
		controls.add(Box.createRigidArea(new Dimension(0, 30)));
		controls.add(scaleLabel);
		controls.add(scaleSlider);
		controls.add(Box.createRigidArea(new Dimension(0, 30)));
		controls.add(timerLabel);
		controls.add(timerSlider);
		controls.add(Box.createRigidArea(new Dimension(0, 30)));
		controls.add(peopleLabel);
		controls.add(peopleSlider);
		controls.add(Box.createRigidArea(new Dimension(0, 30)));
		controls.add(pauseButton);
		controls.add(playButton);
		controls.add(restartButton);

		BoxLayout layout = new BoxLayout(this, BoxLayout.X_AXIS);
		setLayout(layout);
		add(Box.createRigidArea(new Dimension(WIDTH * 2, 0)));

		add(controls);

		timer = new Timer(DELAY, new CoronaListener());
		setPreferredSize(new Dimension(WIDTH * 2 + LAYOUT_WIDTH, HEIGHT + (numPeople / graphScale)));

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
			int temp = HEIGHT + (numPeople / graphScale);

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
		page.drawLine(0, HEIGHT + ((numPeople - CAPACITY) / graphScale), WIDTH,
				HEIGHT + ((numPeople - CAPACITY) / graphScale));

		// Draw graph (distanced)
		for (int i = WIDTH + 1; i < WIDTH + tick; i++) {
			int temp = HEIGHT + (numPeople / graphScale);

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
			page.drawLine(i, Math.min(temp, HEIGHT), i, temp);
		}
		page.setColor(Color.black);
		page.drawLine(WIDTH, HEIGHT + ((numPeople - CAPACITY) / graphScale), WIDTH * 2,
				HEIGHT + ((numPeople - CAPACITY) / graphScale));

		page.drawLine(WIDTH, 0, WIDTH, HEIGHT + (numPeople / graphScale));
	}

	public void reinitialize() {
		timerTriggers = 0;
		tick = 0;
		people = new Person[numPeople];
		peopleDistanced = new Person[numPeople];
		graph = new int[4][WIDTH];
		graphDistanced = new int[4][WIDTH];

		int infected = (int) (Math.random() * numPeople);
		for (int i = 0; i < people.length; i++) {
			people[i] = new Person((int) (Math.random() * (WIDTH - 10)), (int) (Math.random() * (HEIGHT - 10)),
					i == infected ? true : false, false, false);
		}

		infected = (int) (Math.random() * (numPeople - IMMOBILE)) + IMMOBILE;
		for (int i = 0; i < people.length; i++) {
			if (i < IMMOBILE) {
				peopleDistanced[i] = new Person((int) (Math.random() * (WIDTH - 10)) + WIDTH,
						(int) (Math.random() * (HEIGHT - 10)), false, true, true);
			} else {
				peopleDistanced[i] = new Person((int) (Math.random() * (WIDTH - 10)) + WIDTH,
						(int) (Math.random() * (HEIGHT - 10)), i == infected ? true : false, false, true);
			}
		}
		playButton.setEnabled(true);
		pauseButton.setEnabled(true);
		restartButton.setEnabled(true);
		scaleSlider.setEnabled(false);
		timerSlider.setEnabled(true);
		peopleSlider.setEnabled(false);
	}

	private class SliderListener implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			if (e.getSource() == scaleSlider || e.getSource() == peopleSlider) {
				playButton.setEnabled(false);
				pauseButton.setEnabled(false);
			}
			graphScale = scaleSlider.getValue();
			scaleLabel.setText("Graph Scaling: " + graphScale);
			Corona.frame.setSize(new Dimension(WIDTH * 2 + LAYOUT_WIDTH + 16, HEIGHT + (numPeople / graphScale) + 39));
			timer.setDelay(timerSlider.getValue());
			timerLabel.setText("Tickrate: " + timerSlider.getValue());

			numPeople = peopleSlider.getValue();
			peopleLabel.setText("People: " + numPeople);
		}
	}

	private class ControlListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == playButton) {
				timer.restart();
				paused = false;
			} else if (e.getSource() == pauseButton) {
				timer.stop();
				paused = true;
			} else {
				timer.restart();
				reinitialize();
			}

			scaleSlider.setEnabled(paused);
			peopleSlider.setEnabled(paused);

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
