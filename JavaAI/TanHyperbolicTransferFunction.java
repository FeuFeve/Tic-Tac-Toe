package ai;

import java.io.Serializable;

public class TanHyperbolicTransferFunction implements TransferFunction, Serializable {


	public double evalute(double value) {
		return (Math.tanh(value));
	}

	public double evaluteDerivate(double value) {
		return (1.0 - Math.pow(evalute(value), 2));
	}
}
