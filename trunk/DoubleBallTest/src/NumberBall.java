import java.util.ArrayList;
import java.util.List;

public class NumberBall {
	private int number;
	int cycleCount;
	float cycleScale;
	List<Integer> cycleList = new ArrayList<Integer>();

	public NumberBall(int i) {
		number = i;
	}

	public int getNumber() {
		return number;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NumberBall) {
			NumberBall ball = (NumberBall) obj;
			return this.number == ball.number;
		}
		return false;
	}

}
