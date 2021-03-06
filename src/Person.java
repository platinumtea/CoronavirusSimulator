
public class Person {
	private double x, y, xVel, yVel;
	private int status; // 0 for healthy, 1 for infected, 2 for hospitalized , 3 for recovered, 4 for
						// dead
	private int ticksSinceInfected, ticksSinceHospitalized;
	private boolean firstInfected, ignore, immobile, isRight;
	private double risk;

	public Person(int x, int y, boolean infected, boolean immobile, boolean isRight) {
		this.x = x;
		this.y = y;
		this.immobile = immobile;
		this.isRight = isRight;
		risk = Math.random();
		xVel = Math.random() * 4.0 - 2.0;
		yVel = Math.sqrt(4 - Math.pow(xVel, 2)) * (Math.random() < 0.5 ? -1.0 : 1.0);
		ticksSinceInfected = 0;
		ticksSinceHospitalized = 0;
		status = (infected ? 1 : 0);
		firstInfected = infected;
	}

	public void tick() {
		if (status != 2 && status != 4 && !immobile) {
			if (!isRight) {
				if (x + xVel + 10 > CoronaPanel.WIDTH || x + xVel < 0) {
					xVel = -xVel;
				}
				if (y + yVel + 10 > CoronaPanel.HEIGHT || y + yVel < 0) {
					yVel = -yVel;
				}
			} else {
				if (x + xVel + 10 > CoronaPanel.WIDTH * 2 || x + xVel < CoronaPanel.WIDTH) {
					xVel = -xVel;
				}
				if (y + yVel + 10 > CoronaPanel.HEIGHT || y + yVel < 0) {
					yVel = -yVel;
				}
			}
			x += xVel;
			y += yVel;
		}
		if (!firstInfected) {
			if (status == 1) {
				ticksSinceInfected++;
				if (ticksSinceInfected > 800) {
					status = 3;
				} else if (risk < 0.02) {
					status = 4;
				}
			} else if (status == 2) {
				ticksSinceHospitalized++;
				if (ticksSinceHospitalized > 500 && risk < 0.5) {
					status = 4;
				} else if (ticksSinceHospitalized > 500) {
					status = 3;
				}
			}
		}
	}

	public void check(Person p) {
		if (!ignore) {
			if (Math.sqrt(Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2)) < 10) {
				if (p.getStatus() == 1 && Math.random() < 0.85 && status == 0) {
					status = 1;
				}
				if (!immobile && status != 2) {
					xVel = (Math.random() * 2 * (x - p.getX() < 0 ? -1.0 : 1.0));
					yVel = (Math.sqrt(4 - Math.pow(xVel, 2))) * (y - p.getY() < 0 ? -1.0 : 1.0);
					p.ignoreCheck();
				}
				if (firstInfected) {
					firstInfected = false;
				}
			}
		} else {
			ignore = false;
		}
	}

	public void ignoreCheck() {
		ignore = true;
	}

	public void hospitalize() {
		if (!firstInfected) {
			status = 2;
		}
	}

	public void kill() {
		status = 4;
	}

	public int getStatus() {
		return status;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public boolean isInfected() {
		return (status == 1 ? true : false);
	}
}
