import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class CoronaPanel extends JPanel {
	private final int DELAY = 10, PEOPLE = 300, RECORD_RATE = 2, GRAPH_SCALING = 2, IMMOBILE = 220, CAPACITY = 50;
	static final int WIDTH = 600, HEIGHT = 800;
	Timer timer;
	private int tick, timerTriggers;
	private int numHospitalized, numHospitalizedDistanced;

	Person[] people, peopleDistanced;
	int[][] graph, graphDistanced;

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
		timer = new Timer(DELAY, new CoronaListener());
		setPreferredSize(new Dimension(WIDTH * 2, HEIGHT + (PEOPLE / GRAPH_SCALING)));
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
			int temp = HEIGHT + (PEOPLE / GRAPH_SCALING);

			page.setColor(Color.pink);
			page.drawLine(i, temp - (graph[2][i] / GRAPH_SCALING), i, temp);
			temp -= (graph[2][i] / GRAPH_SCALING);

			page.setColor(Color.red);
			page.drawLine(i, temp - (graph[1][i] / GRAPH_SCALING), i, temp);
			temp -= (graph[1][i] / GRAPH_SCALING);

			page.setColor(Color.green);
			page.drawLine(i, temp - (graph[0][i] / GRAPH_SCALING), i, temp);
			temp -= (graph[0][i] / GRAPH_SCALING);


			page.setColor(Color.blue);
			page.drawLine(i, temp - (graph[3][i] / GRAPH_SCALING), i, temp);
			temp -= (graph[3][i] / GRAPH_SCALING);

			page.setColor(Color.gray);
			page.drawLine(i, HEIGHT, i, temp);
		}
		page.setColor(Color.black);
		page.drawLine(0, HEIGHT + ((PEOPLE - CAPACITY) / GRAPH_SCALING), WIDTH, HEIGHT + ((PEOPLE - CAPACITY) / GRAPH_SCALING));

		// Draw graph (distanced)
		for (int i = WIDTH; i < WIDTH + tick; i++) {
			int temp = HEIGHT + (PEOPLE / GRAPH_SCALING);

			page.setColor(Color.red);
			page.drawLine(i, temp - (graphDistanced[1][i - WIDTH] / GRAPH_SCALING), i, temp);
			temp -= (graphDistanced[1][i - WIDTH] / GRAPH_SCALING);

			page.setColor(Color.green);
			page.drawLine(i, temp - (graphDistanced[0][i - WIDTH] / GRAPH_SCALING), i, temp);
			temp -= (graphDistanced[0][i - WIDTH] / GRAPH_SCALING);

			page.setColor(Color.pink);
			page.drawLine(i, temp - (graphDistanced[2][i - WIDTH] / GRAPH_SCALING), i, temp);
			temp -= (graphDistanced[2][i - WIDTH] / GRAPH_SCALING);

			page.setColor(Color.blue);
			page.drawLine(i, temp - (graphDistanced[3][i - WIDTH] / GRAPH_SCALING), i, temp);
			temp -= (graphDistanced[3][i - WIDTH] / GRAPH_SCALING);

			page.setColor(Color.gray);
			page.drawLine(i, HEIGHT, i, temp);
		}
		page.setColor(Color.black);
		page.drawLine(WIDTH, HEIGHT + ((PEOPLE - CAPACITY) / GRAPH_SCALING), WIDTH * 2, HEIGHT + ((PEOPLE - CAPACITY) / GRAPH_SCALING));
	}

	private class CoronaListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < people.length; i++) {
				people[i].tick();
				if(people[i].isInfected() && i < PEOPLE * 0.2 && numHospitalized < CAPACITY) {
					people[i].hospitalize();
					numHospitalized++;
				} else if (people[i].isInfected() && i < PEOPLE * 0.2){
					people[i].kill();
				}
				peopleDistanced[i].tick();
				if(peopleDistanced[i].isInfected() && i < PEOPLE * 0.2 && numHospitalizedDistanced < CAPACITY) {
					peopleDistanced[i].hospitalize();
					numHospitalizedDistanced++;
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
