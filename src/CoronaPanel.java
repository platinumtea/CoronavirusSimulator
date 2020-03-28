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
	int[] healthy, infected, hospitalized, recovered, dead;

	public CoronaPanel() {
		timerTriggers = 0;
		tick = 0;
		people = new Person[PEOPLE];
		healthy = new int[WIDTH];
		infected = new int[WIDTH];
		hospitalized = new int[WIDTH];
		recovered = new int[WIDTH];
		dead = new int[WIDTH];
		int infected = (int) (Math.random() * 100);
		for (int i = 0; i < people.length; i++) {
			people[i] = new Person((int) (Math.random() * (WIDTH - 10)), (int) (Math.random() * (HEIGHT - 10)),
					i == infected ? true : false);
		}
		timer = new Timer(DELAY, new CoronaListener());
		setPreferredSize(new Dimension(WIDTH, HEIGHT + (PEOPLE / GRAPH_SCALING)));
		setBackground(Color.white);
		timer.start();
	}

	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		for (int i = 0; i < people.length; i++) {
			switch (people[i].getStatus()) {
			case 0:
				page.setColor(Color.green);
				if (timerTriggers % RECORD_RATE == 0)
					healthy[tick]++;
				break;
			case 1:
				page.setColor(Color.red);
				if (timerTriggers % RECORD_RATE == 0)
					infected[tick]++;
				break;
			case 2:
				page.setColor(Color.pink);
				if (timerTriggers % RECORD_RATE == 0)
					hospitalized[tick]++;
				break;
			case 3:
				page.setColor(Color.blue);
				if (timerTriggers % RECORD_RATE == 0)
					recovered[tick]++;
				break;
			case 4:
				page.setColor(Color.gray);
				if (timerTriggers % RECORD_RATE == 0)
					dead[tick]++;
				break;
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
			page.drawLine(i, temp - (infected[i] / GRAPH_SCALING), i, temp);
			temp -= (infected[i] / GRAPH_SCALING);

			page.setColor(Color.green);
			page.drawLine(i, temp - (healthy[i] / GRAPH_SCALING), i, temp);
			temp -= (healthy[i] / GRAPH_SCALING);

			page.setColor(Color.pink);
			page.drawLine(i, temp - (hospitalized[i] / GRAPH_SCALING), i, temp);
			temp -= (hospitalized[i] / GRAPH_SCALING);

			page.setColor(Color.blue);
			page.drawLine(i, temp - (recovered[i] / GRAPH_SCALING), i, temp);
			temp -= (recovered[i] / GRAPH_SCALING);

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
				healthy = new int[WIDTH];
				infected = new int[WIDTH];
				hospitalized = new int[WIDTH];
				recovered = new int[WIDTH];
				dead = new int[WIDTH];
				System.out.println("RESET");
			}
		}
	}
}
