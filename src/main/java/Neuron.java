import java.util.List;
import java.util.Random;

public final class Neuron {

    public Neuron(Random random, List<Neuron> inputs) {
        this.random = random;
        this.inputs = inputs;
        this.size = inputs.size();
    }

    Random random;

    List<Neuron> inputs;
    int size;

    /**
     * Sample computed unless it should be computed again.
     */
    boolean output = false;

    /**
     * Sample a new value for the output.
     */
    public void sample() {
        float avg = this.getAverage();
        this.output = random.nextFloat() < this.getAverage();
    }

    /**
     * @return Average sample value of neurons input to this neuron.
     */
    private float getAverage() {
        int numOn = 0;
        for (int i = 0; i < this.size; i++) {
            if (this.inputs.get(i).output) {
                numOn += 1;
            }
        }
        return numOn / this.size;
    }
}
