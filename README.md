# Jeelight

`Jeelight` is a simple implementation of [Yeelight Inter operation specification](https://www.yeelight.com/download/Yeelight_Inter-Operation_Spec.pdf).
The aim of jeelight is to manage for you the discovery and communication with differents `Yeelight` devices.

## Discovering devices

The device discovery is based on `SSDP protocol`. 
`Jeelight` provide a simple implementation.

```java
List<Light> devices = new ArrayList<>();
SSDPClient client = new SSDPClient(5000,'wifi_bulb',1982);
client.addListener(new PropertyChangeListener() {
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (SSDPClient.ADD.equals(evt.getPropertyName())) {
			Light l = (Light) evt.getNewValue();
			devices.add(l);					
		}
	}
});
client.startDiscovering();
```

With this simple code, you start discovering new devices on network and add it in a list.

## Communication

`Jeelight` offers two way to communicate with `Yeelight` devices. An expert way, where you control each data asynchronously and an easy way where you call methods in a synchronnous way.

### The expert way

For the expert way, you need to get a `MessageManager`.

Then you interact in this way:

```java
Light light = ...
MessageManager manager = new MessageManager(l);
Future<Boolean> future = manager.send(Method.method, param1, param2,...);
while(!future.isDone()){// Wait}
// Command was executed, can get the result
if(future.get()){
	// Command correctly executed
} else {
	// Command not executed
}

// Or in this way
future = manager.send(Method.method, new Object[]{param1, param2,...});
while(!future.isDone()){// Wait}
// Command was executed, can get the result
if(future.get()){
	// Command correctly executed
} else {
	// Command not executed
}
```

When an operation modify a parameter of the light, the object is modified when the light send a notification, no need to directly modify object.

### The simple way

With the simple way, no need to manage asynchronously your data, everithing is encapsulate in the `EasyLight` object.

```java
EasyLight eLight = new EasyLight(light);
//Call the method
eLight.toggle();
```
 I most of the cases, the method call return the new value of the modified attribute.
 
When an operation modify a parameter of the light, the object is modified when the light send a notification, no need to directly modify object.
 
## Listen to light modification
 
Because Lights are in network, they can be modified by someone else, so you need to listen your lights to be updated with its real state.

```java
Light light = ...;

light.addListener(new PropertyChangeListener() {
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (null != evt) {
			// manage the event.
			// the property name is the modified property annotated in the Light object
			// the old value is the old value (obviously)
			// the new value is the new value
		}
	}
});
```

Don't forger to remove the listener.

# TODO

Actually I need to manage `adjust`, `flows` and `music` methods.

I actually need to extends test coverage.

# Conclusion
Try it yourself and enjoy