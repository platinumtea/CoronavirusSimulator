
public class Person {
	private int x, y, xVel, yVel;
	private int status; // 0 for healthy, 1 for infected, 2 for hospitalized , 3 for recovered, 4 for
						// dead
	private int ticksSinceInfected;
	private boolean firstInfected;
	private double risk;

	public Person(int x, int y, boolean infected) {
		this.x = x;
		this.y = y;
		risk = Math.random();
		xVel = (int) (Math.random() * 4 - 2);
		yVel = (int) (Math.sqrt(8 - Math.pow(xVel, 2))) * (Math.random() < 0.5 ? -1 : 1);
		ticksSinceInfected = 0;
		status = (infected ? 1 : 0);
		firstInfected = infected;
	}

	public void tick() {
		if (status != 2 && status != 4) {
			if (x + xVel + 10 > CoronaPanel.WIDTH || x + xVel < 0) {
				xVel = -xVel;
			}
			if (y + yVel + 10 > CoronaPanel.HEIGHT || y + yVel < 0) {
				yVel = -yVel;
			}
			x += xVel;
			y += yVel;
		}
		if (!firstInfected) {
			if (status == 1) {
				ticksSinceInfected++;
				if (ticksSinceInfected >= 100 && ticksSinceInfected <= 800 && risk < 0.2) {
					status = 2;
				} else if (ticksSinceInfected > 800) {
					status = 3;
				} else if (risk < 0.02) {
					status = 4;
				}
			} else if (status == 2) {
				if (risk < 0.1) {
					status = 4;
				} else {
					status = 0;
				}
			}
		}
	}

	public void check(Person p) {
		if (Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2)) < 10) {
			if (p.getStatus() == 1 && Math.random() < 0.85) {
				status = 1;
			}
			xVel = (int) (Math.random() * 4 - 2);
			yVel = (int) (Math.sqrt(8 - Math.pow(xVel, 2))) * (Math.random() < 0.5 ? -1 : 1);
		}
	}

	public void hospitalize() {
		status = 2;
	}

	public int getStatus() {
		return status;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isInfected() {
		return (status == 1 ? true : false);
	}
}
