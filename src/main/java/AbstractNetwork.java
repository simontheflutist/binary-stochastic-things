import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public abstract class AbstractNetwork<I, O> {
    final int numIn;
    final List<Neuron> inputNeurons;

    final int numMid;
    final List<Neuron> middleNeurons;

    final int numOut;
    final List<Neuron> outputNeurons;

    protected AbstractNetwork(int numIn,int numOut, int numMid,
                              Supplier<Random> randomSupplier) {
        this.numIn = numIn;
        this.numMid = numMid;
        this.numOut = numOut;

        this.inputNeurons = this.createNeurons(numIn, ArrayList::new, randomSupplier);
        this.middleNeurons = this.createNeurons(numMid, ArrayList::new, randomSupplier);
        this.outputNeurons = this.createNeurons(numOut, ArrayList::new, randomSupplier);
    }

    private List<Neuron> createNeurons(int num,
                                       Supplier<List<Neuron>> listSupplier,
                                       Supplier<Random> randomSupplier) {
        List<Neuron> list = listSupplier.get();
        for (int i = 0; i < num; i++) {
            list.add(new Neuron(randomSupplier.get(), listSupplier.get()));
        }
        return list;
    }

    public O sample(I input) {
        this.initializeInput(input);
        this.sampleMiddle();
        this.sampleOutput();
        return this.renderOutput();
    }

    private void initializeInput(I input) {
        boolean[] encoding = this.encode(input);

        if (encoding.length != this.numIn) {
            throw new IllegalStateException();
        }

        for (int i = 0; i < this.numIn; i++) {
            this.inputNeurons.get(i).output = encoding[i];
        }
    }

    private void sampleMiddle() {
        for (Neuron neuron :
                this.middleNeurons) {
            neuron.sample();
        }
    }

    private void sampleOutput() {
        for (Neuron neuron :
                this.outputNeurons) {
            neuron.sample();
        }
    }

    private O renderOutput() {
        boolean[] encoding = new boolean[this.numOut];

        for (int i = 0; i < this.numOut; i++) {
            encoding[i] = this.outputNeurons.get(i).output;
        }

        return this.decode(encoding);
    }

    protected abstract boolean[] encode(I input);

    protected abstract O decode(boolean[] encoding);
}
