import java.awt.*;
import javax.swing.*;

public class Corona {
	static JFrame frame = new JFrame("Coronavirus Simulation");

	public static void main(String[] args) {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().add(new CoronaPanel());
		frame.pack();
		frame.setVisible(true);
		frame.setMinimumSize(new Dimension(CoronaPanel.WIDTH, CoronaPanel.HEIGHT));
		frame.setResizable(false);
	}
}
