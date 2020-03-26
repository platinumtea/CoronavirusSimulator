import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Corona {
	public static void main(String[] args) {
		JFrame frame = new JFrame("Coronavirus Simulation");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().add(new CoronaPanel());
		frame.pack();
		frame.setVisible(true);
	}
}
