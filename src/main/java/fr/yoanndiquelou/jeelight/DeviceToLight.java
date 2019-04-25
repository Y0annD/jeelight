package fr.yoanndiquelou.jeelight;

import java.util.function.Function;

import fr.yoanndiquelou.jeelight.model.Light;
import fr.yoanndiquelou.jeelight.ssdp.Device;

public class DeviceToLight implements Function<Device, Light> {

	public Light apply(Device t) {
		Light l = new Light();
		l.setIp(t.getIPAddress());
		l.setModel(t.getInfo("MODEL"));
		l.setFirmware(Integer.valueOf(t.getInfo("FW_VER")));
		l.setPower("on".equals(t.getInfo("POWER")));
		l.setBrightness(Integer.valueOf(t.getInfo("BRIGHT")));
		l.setColorMode(Integer.valueOf(t.getInfo("COLOR_MODE")));
		l.setCt(Integer.valueOf(t.getInfo("CT")));
		l.setRGB(Long.valueOf(t.getInfo("RGB")));
		l.setSaturation(Integer.valueOf(t.getInfo("SAT")));
		l.setName(t.getInfo("NAME"));
		return l;
	}

}
