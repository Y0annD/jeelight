package fr.yoanndiquelou.jeelight.communication;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

import fr.yoanndiquelou.jeelight.model.Command;
import fr.yoanndiquelou.jeelight.model.Duo;

public class SendRunnable implements Callable<Boolean>{
	/** Duo. */
	Duo<Command, Boolean> mDuo;
	/** BufferOutputStream. */
	private DataOutputStream mOutStream;
	
	public SendRunnable(Duo<Command, Boolean> duo, DataOutputStream outStream) {
		mDuo = duo;
		mOutStream = outStream;
	}
	

	@Override
	public Boolean call() throws Exception {
		synchronized(mDuo) {
			try {
				mOutStream.write(mDuo.getK().toString().getBytes());
				mOutStream.flush();

				while(null == mDuo.getV()) {
					mDuo.wait();
				}
				return mDuo.getV();
			} catch (IOException e) {
				return false;
			}
		}
	}

}
