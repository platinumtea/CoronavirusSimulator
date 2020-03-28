import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class CoronaPanel extends JPanel {
	private final int DELAY = 10, PEOPLE = 300, RECORD_RATE = 2, GRAPH_SCALING = 2;
	static final int WIDTH = 600, HEIGHT = 800;
	Timer timer;
	private int tick, timerTriggers;

	Person[] people;
	int[][] graph;

	public CoronaPanel() {
		timerTriggers = 0;
		tick = 0;
		people = new Person[PEOPLE];
		graph = new int[4][WIDTH];

		int infected = (int) (Math.random() * 100);
		for (int i = 0; i < people.length; i++) {
			people[i] = new Person((int) (Math.random() * (WIDTH - 10)), (int) (Math.random() * (HEIGHT - 10)),
					i == infected ? true : false);
		}
		timer = new Timer(DELAY, new CoronaListener());
		setPreferredSize(new Dimension(WIDTH * 2, HEIGHT + (PEOPLE / GRAPH_SCALING)));
		setBackground(Color.white);
		timer.start();
	}

	public void paintComponent(Graphics page) {
		super.paintComponent(page);
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
			page.fillOval(people[i].getX(), people[i].getY(), 10, 10);
			for (int j = 0; j < people.length; j++) {
				if (i != j) {
					people[i].check(people[j]);
				}
			}
		}
		for (int i = 0; i < tick; i++) {
			int temp = HEIGHT + (PEOPLE / GRAPH_SCALING);

			page.setColor(Color.red);
			page.drawLine(i, temp - (graph[1][i] / GRAPH_SCALING), i, temp);
			temp -= (graph[1][i] / GRAPH_SCALING);

			page.setColor(Color.green);
			page.drawLine(i, temp - (graph[0][i] / GRAPH_SCALING), i, temp);
			temp -= (graph[0][i] / GRAPH_SCALING);
			
			page.setColor(Color.pink);
			page.drawLine(i, temp - (graph[2][i] / GRAPH_SCALING), i, temp);
			temp -= (graph[2][i] / GRAPH_SCALING);

			page.setColor(Color.blue);
			page.drawLine(i, temp - (graph[3][i] / GRAPH_SCALING), i, temp);
			temp -= (graph[3][i] / GRAPH_SCALING);

			page.setColor(Color.gray);
			page.drawLine(i, HEIGHT, i, temp);
		}
	}

	private class CoronaListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < people.length; i++) {
				people[i].tick();
			}
			repaint();
			timerTriggers++;
			if (timerTriggers % RECORD_RATE == 0) {
				tick++;
			}
			if (tick > 599) {
				tick = 0;
				graph = new int[4][WIDTH];
			}
		}
	}
}
