import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CoronaPanel extends JPanel {
	private final int DELAY = 10, PEOPLE = 100;
	static final int WIDTH = 600, HEIGHT = 800;
	Timer timer;
	private int tick, timerTriggers;

	Person[] people;
	int[] healthy, infected, hospitalized, recovered, dead;

	public CoronaPanel() {
		timerTriggers = 0;
		tick = 0;
		people = new Person[PEOPLE];
		healthy = new int[600];
		infected = new int[600];
		hospitalized = new int[600];
		recovered = new int[600];
		dead = new int[600];
		int infected = (int) (Math.random() * 100);
		for (int i = 0; i < people.length; i++) {
			people[i] = new Person((int) (Math.random() * WIDTH - 10), (int) (Math.random() * HEIGHT - 10),
					i == infected ? true : false);
		}
		timer = new Timer(DELAY, new CoronaListener());
		setPreferredSize(new Dimension(WIDTH, HEIGHT + PEOPLE));
		setBackground(Color.white);
		timer.start();
	}

	public void paintComponent(Graphics page) {
		super.paintComponent(page);
		for (int i = 0; i < people.length; i++) {
			switch (people[i].getStatus()) {
			case 0:
				page.setColor(Color.green);
				if (timerTriggers % 3 == 0)
					healthy[tick]++;
				break;
			case 1:
				page.setColor(Color.red);
				if (timerTriggers % 3 == 0)
					infected[tick]++;
				break;
			case 2:
				page.setColor(Color.pink);
				if (timerTriggers % 3 == 0)
					hospitalized[tick]++;
				break;
			case 3:
				page.setColor(Color.blue);
				if (timerTriggers % 3 == 0)
					recovered[tick]++;
				break;
			case 4:
				page.setColor(Color.gray);
				if (timerTriggers % 3 == 0)
					dead[tick]++;
				break;
			}
			page.fillOval(people[i].getX(), people[i].getY(), 10, 10);
			for (int j = 0; j < people.length; j++) {
				if(i != j) {
					people[i].check(people[j]);
				}
			}
		}
		for (int i = 0; i < tick; i++) {
			int temp = HEIGHT + PEOPLE;

			page.setColor(Color.red);
			page.drawLine(i, temp - infected[i], i, temp);
			temp -= infected[i];
			page.setColor(Color.green);
			page.drawLine(i, temp - healthy[i], i, temp);
			temp -= healthy[i];

			page.setColor(Color.pink);
			page.drawLine(i, temp - hospitalized[i], i, temp);
			temp -= hospitalized[i];

			page.setColor(Color.blue);
			page.drawLine(i, temp - recovered[i], i, temp);
			temp -= recovered[i];

			page.setColor(Color.gray);
			page.drawLine(i, temp - dead[i], i, temp);
		}
	}

	private class CoronaListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < people.length; i++) {
				people[i].tick();
			}
			repaint();
			timerTriggers++;
			if (timerTriggers % 3 == 0) {
				tick++;
			}
			if (tick > 599) {
				tick = 0;
				healthy = new int[600];
				infected = new int[600];
				hospitalized = new int[600];
				recovered = new int[600];
				dead = new int[600];
				System.out.println("RESET");
			}
		}
	}
}
