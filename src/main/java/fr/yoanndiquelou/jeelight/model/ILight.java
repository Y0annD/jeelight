package fr.yoanndiquelou.jeelight.model;

import java.util.List;

import fr.yoanndiquelou.jeelight.exception.CommandException;

public interface ILight {

	/**
	 * Get property method.
	 * 
	 * @param properties array of property to retrieve
	 * @return values of those properties, or empty string if not exist
	 * @throws CommandException command unavailable
	 */
	Object[] getProp(String... properties) throws CommandException;

	/**
	 * Toggle Light status.
	 * 
	 * @return new status of light
	 * @throws CommandException unavailable method
	 */
	boolean toggle() throws CommandException;

	/**
	 * Set the current state as default. Persisted in light memory.
	 * 
	 * @throws CommandException unavailable method
	 */
	void setDefault() throws CommandException;

	/**
	 * Start color flow.
	 * 
	 * @param end  end mode
	 * @param flow list of changes in flow
	 * @throws CommandException unavailable method
	 */
	void startCf(ColorFlowEnd end, List<FlowColor> flow) throws CommandException;

	/**
	 * Stop color flow.
	 * 
	 * @throws CommandException unavailable method
	 */
	void stopCf() throws CommandException;

	/**
	 * Adjust brightness.
	 * 
	 * @param percentage percentage of brightness
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	int adjustBright(int percentage, int duration) throws CommandException;

	/**
	 * Adjust brightness.
	 * 
	 * @param percentage percentage of brightness
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	int bgAdjustBright(int percentage, int duration) throws CommandException;

	/**
	 * Adjust color temperature.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	int adjustColorTemperature(int percentage, int duration) throws CommandException;

	/**
	 * Adjust color.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	int adjustColor(int percentage, int duration) throws CommandException;

	/**
	 * Adjust color.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	int bgAdjustColor(int percentage, int duration) throws CommandException;

	/**
	 * Adjust color temperature.
	 * 
	 * @param percentage percentage of color temperature
	 * @param duration   duration of changement
	 * @return new brightness
	 * @throws CommandException unavailable method
	 */
	int bgAdjustColorTemperature(int percentage, int duration) throws CommandException;

	/**
	 * Define color temperature.
	 * 
	 * @param temperature color temperature Should be:
	 *                    <ul>
	 *                    <li>Higher or equal to 1700K
	 *                    <li>
	 *                    <li>Lower or equal to 6500K</li>
	 *                    </ul>
	 * @param effect      effect
	 * @param duration    Should be higher or equals than 30 ms
	 * @throws CommandException unavailable method
	 */
	int setCtAbx(int temperature) throws CommandException;

	/**
	 * Define background color temperature.
	 * 
	 * @param temperature color temperature Should be:
	 *                    <ul>
	 *                    <li>Higher or equal to 1700K
	 *                    <li>
	 *                    <li>Lower or equal to 6500K</li>
	 *                    </ul>
	 * @param effect      effect
	 * @param duration    Should be higher or equals than 30 ms
	 * @throws CommandException unavailable method
	 */
	int setBgCtAbx(int temperature) throws CommandException;

	/**
	 * Set rgb color.
	 * 
	 * @param red   red value
	 * @param green green value
	 * @param blue  blue value
	 * @return new color as following [red, green, blue]
	 * @throws CommandException something goes wrong
	 */
	int[] setRgb(int red, int green, int blue) throws CommandException;

	/**
	 * set rgb value.
	 * 
	 * @param mergedValue rgb value as 0x(R)(G)(B)
	 * @return new color
	 * @throws CommandException unavailable method
	 */
	int setRgb(int mergedValue) throws CommandException;

	/**
	 * Set hue and saturation.
	 * 
	 * @param hue hue value
	 * @param sat saturation value
	 * @throws CommandException something goes wrong
	 */
	void setHsv(int hue, int sat) throws CommandException;

	/**
	 * Set hue and saturation.
	 * 
	 * @param hue hue value
	 * @param sat saturation value
	 * @throws CommandException something goes wrong
	 */
	void setHsv(Method method, int hue, int sat) throws CommandException;

	/**
	 * Set brightness.
	 * 
	 * @param bright brighness value
	 * @return new brightness
	 * @throws CommandException something goes wrong
	 */
	int setBright(int bright) throws CommandException;

	/**
	 * Set light power.
	 * 
	 * @param power true to switch on, false to switch off
	 * @return new power value
	 * @throws CommandException command could not be executed
	 */
	boolean setPower(boolean power) throws CommandException;

	/**
	 * Set scene with 2 arguments.
	 * 
	 * @param scClass    scene class
	 * @param val1
	 *                   <ul>
	 *                   <li>Color for COLOR class</li>
	 *                   <li>Color temperature for CT class</li>
	 *                   </ul>
	 * @param brightness brightness
	 * @throws CommandException command could not be executed
	 */
	void setScene(SceneClass scClass, int val1, int brightness) throws CommandException;

	/**
	 * Set scene with 2 arguments.
	 * 
	 * @param scClass    scene class
	 * @param val1
	 *                   <ul>
	 *                   <li>Color for COLOR class</li>
	 *                   <li>Color temperature for CT class</li>
	 *                   </ul>
	 * @param brightness brightness
	 * @throws CommandException command could not be executed
	 */
	void setBgScene(SceneClass scClass, int val1, int brightness) throws CommandException;

	void setScene(SceneClass scClass, int val1, int val2, Object val3) throws CommandException;

	void setBgScene(SceneClass scClass, int val1, int val2, Object val3) throws CommandException;

	/**
	 * Set scene with 3 arguments.
	 * 
	 * @param scClass class, might be color flow
	 * @param val1
	 *                <ul>
	 *                <li>Hue for HSV class</li>
	 *                <li>for CF class</li>
	 *                </ul>
	 * @param val2
	 *                <ul>
	 *                <li>Saturation for HSV class</li>
	 *                <li>for CF class</li>
	 *                </ul>
	 * @param val3
	 *                <ul>
	 *                <li>brightness for HSV class</li>
	 *                <li>Flow for CF class</li>
	 *                </ul>
	 * @throws CommandException
	 */
	void setScene(Method method, SceneClass scClass, int val1, int val2, Object val3) throws CommandException;

	/**
	 * Set time before switch off.
	 * 
	 * @param time time before switch off
	 * @return new time
	 * @throws CommandException unavailable method
	 */
	int setCron(int time) throws CommandException;

	/**
	 * Set time before switch off.
	 * 
	 * @return time before switch off
	 * @throws CommandException unavailable method
	 */
	int getCron() throws CommandException;

	/**
	 * Delete cron task.
	 * 
	 * @throws CommandException unavailable method
	 */
	void delCron() throws CommandException;

	/**
	 * Set music mode.
	 * <p>
	 * Do not forget to start a server.
	 * </p>
	 * 
	 * @param turnOn turn on or turn off
	 * @param host   host the IP address of the music server
	 * @param port   the TCP port music application is listening on
	 * @throws CommandException unavailable method
	 */
	void setMusic(boolean turnOn, String host, int port) throws CommandException;

	/**
	 * Adjust property.
	 * 
	 * @param adjust   adjust type
	 * @param property property to adjust. On eof
	 *                 <ul>
	 *                 <li>ct</li>
	 *                 <li>bright</li>
	 *                 <li>color</li>
	 *                 </ul>
	 * @throws CommandException unavailable method
	 */
	void setAdjust(AdjustType adjust, String property) throws CommandException;

	/**
	 * Toggle Light status.
	 * 
	 * @return new status of light
	 * @throws CommandException unavailable method
	 */
	boolean bgToggle() throws CommandException;

	/**
	 * Set background rgb value.
	 * 
	 * @param red   red value
	 * @param green green value
	 * @param blue  blue value
	 * @return new value as following [red, green, blue]
	 * @throws CommandException something goes wrong
	 */
	int[] setBgRgb(int red, int green, int blue) throws CommandException;

	/**
	 * Set background rgb value.
	 * 
	 * @param mergedValue merged background rgb value
	 * @return new background rgb value
	 * @throws CommandException something goes wrong
	 */
	int setBgRgb(int mergedValue) throws CommandException;

	/**
	 * Set hue and saturation for background.
	 * 
	 * @param hue background hue
	 * @param sat background saturation
	 * @throws CommandException something goes wrong
	 */
	void setBgHsv(int hue, int sat) throws CommandException;

	/**
	 * Set background brigness.
	 * 
	 * @param bright brightness
	 * @return background brightness
	 * @throws CommandException something goes wrong
	 */
	int setBgBright(int bright) throws CommandException;

	/**
	 * Set the background power status.
	 * 
	 * @param power power status
	 * @return background power status
	 * @throws CommandException something goes wrong
	 */
	boolean setBgPower(boolean power) throws CommandException;

	/**
	 * Adjust bg property.
	 * 
	 * @param adjust   adjust type
	 * @param property property to adjust. On of
	 *                 <ul>
	 *                 <li>ct</li>
	 *                 <li>bright</li>
	 *                 <li>color</li>
	 *                 </ul>
	 * @throws CommandException unavailable method
	 */
	void setBgAdjust(AdjustType adjust, String property) throws CommandException;

	/**
	 * Set device name.
	 * 
	 * @param name device name
	 * @return new Device name
	 * @throws CommandException unavailable method
	 */
	String setName(String name) throws CommandException;

	/**
	 * Return the associated light.
	 * 
	 * @return associated light
	 */
	Light getLight();

}