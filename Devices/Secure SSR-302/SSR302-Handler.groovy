 Welcome back, turbozmr2@gmail.com
Logout
My Locations
My Hubs
My Devices
My SmartApps
My Device Handlers
My Publication Requests
Live Logging
Documentation
Secure SSR-302 SavePublishIDE SettingsDevice Type SettingsSimulator 

/**
 *  Copyright 2016 LimeNinja
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  SSR302 Device Handler
 *
 *  Original Author: LimeNinja
 *  Modified by: turbozmr2 - 20160918
 */

metadata {
	definition (name: "Secure SSR-302", namespace: "turbozmr2", author: "turbozmr2") {
    	capability "Thermostat"
        capability "Switch"
		capability "Polling"
        capability "Configuration"
		capability "Refresh"
		capability "Sensor"
        capability "Actuator"
		capability "Temperature Measurement"
        
        command "heatingSetpointUp"
		command "heatingSetpointDown"
        command "setTemperature", ["Number"]
        command "setHumidity", ["Number"]
   		command "ChOn"
		command "ChOff"
		command "HwOn"
		command "HwOff"
        command "heatingActive"
        command "heatingInactive"
        command "setHeatingSetpoint", ["Number"]
        
       	attribute "switch1", "string"
		attribute "switch2", "string"
	}

	simulator {
		// TODO: define status and reply messages here
	}
    
    tiles(scale: 2) {
	multiAttributeTile(name:"temperature", type:"thermostat", width: 6, height: 4) {
		tileAttribute("device.temperature", key: "PRIMARY_CONTROL") {
			attributeState("default", label:'${currentValue}°', /*icon:"st.Home.home1", */backgroundColors:[
			// Celsius Color Range
			[value: 0, color: "#153591"],
			[value: 7, color: "#1e9cbb"],
			[value: 17, color: "#90d2a7"],
			[value: 20, color: "#44b621"],
			[value: 21, color: "#f1d801"],
			[value: 23, color: "#d04e00"],
			[value: 25, color: "#bc2323"],
			// Fahrenheit Color Range
			[value: 40, color: "#153591"],
			[value: 44, color: "#1e9cbb"],
			[value: 59, color: "#90d2a7"],
			[value: 74, color: "#44b621"],
			[value: 84, color: "#f1d801"],
			[value: 92, color: "#d04e00"],
			[value: 96, color: "#ff8426"]
			]
			)
		}
		tileAttribute("device.humidity", key: "SECONDARY_CONTROL") {
			attributeState("level", label:'${currentValue}%')
		}
		tileAttribute("device.thermostatOperatingState", key: "OPERATING_STATE") {
			attributeState("idle", backgroundColor:"#44b621")
			attributeState("heating", backgroundColor:"#ffa81e")
			attributeState("cooling", backgroundColor:"#269bd2")
		}
		tileAttribute("device.thermostatMode", key: "THERMOSTAT_MODE") {
			attributeState("off", label:'${name}')
			attributeState("heat", label:'${name}')
			attributeState("cool", label:'${name}')
			attributeState("auto", label:'${name}')
		}
		tileAttribute("device.heatingSetpoint", key: "HEATING_SETPOINT") {
			attributeState("default", label:'${currentValue}')
		}
		tileAttribute("device.coolingSetpoint", key: "COOLING_SETPOINT") {
			attributeState("default", label:'${currentValue}')
		}
	}
    
    standardTile("switch2", "device.switch2",canChangeIcon: true, width:1, height:1) {
        state "on", label: "HW Status", backgroundColor: "#79b821"
        state "off", label: "HW Status", backgroundColor: "#ffffff"
    }
    standardTile("switch1", "device.switch1",canChangeIcon: true, width:1, height:1) {
        state "on", label: "CH Status", backgroundColor: "#79b821"
        state "off", label: "CH Status", backgroundColor: "#ffffff"
    }
    
    standardTile("water", "device.switch2",canChangeIcon: true, decoration: "flat", width: 2, height: 2) {
        state "on", action:"HwOff", label: "HW", backgroundColor: "#79b821", nextState:"off", icon: "st.Bath.bath6"
        state "off", action:"HwOn", label: "HW", backgroundColor: "#ffffff", nextState:"on", icon: "st.Bath.bath6"

    }
    standardTile("heat", "device.switch1",canChangeIcon: true, decoration: "flat", width: 2, height: 2) {
        state "on", action:"ChOff", label: "CH", backgroundColor: "#79b821", nextState:"off", icon: "st.Home.home29"
        state "off", action:"ChOn", label: "CH", backgroundColor: "#ffffff", nextState:"on", icon: "st.Home.home29"
    }

	standardTile("heatingSetpointUp", "device.heatingSetpoint", decoration: "flat", width: 2, height:1) {
		state "heatingSetpointUp", label:' Heat ', action:"heatingSetpointUp", icon:"st.thermostat.thermostat-up"
	}

	valueTile("heatingSetpoint", "device.heatingSetpoint", inactiveLabel: false, decoration: "flat", width: 2, height:2) {
    	state "heat", label:'${currentValue}°', unit:"C", backgroundColor:"#ff8426"
	}

	valueTile("coolingSetpoint", "device.coolingSetpoint") {
    	state "heat", label:'${currentValue}°', unit:"C", backgroundColors:[
			// Celsius Color Range
			[value: -1, color: "#79b821"],
			[value: 0, color: "#79b821"],
			[value: 1, color: "#90d2a7"],
			[value: 2, color: "#f1d801"],
			[value: 3, color: "#f1d801"],
			[value: 4, color: "#d04e00"],
			[value: 5, color: "#bc2323"]]
	}

	standardTile("heatingSetpointDown", "device.heatingSetpoint", decoration: "flat", width: 2, height: 1) {
		state "heatingSetpointDown", label:' Heat ', action:"heatingSetpointDown", icon:"st.thermostat.thermostat-down"
	}

	standardTile("thermostatMode", "device.thermostatMode", inactiveLabel: true, decoration: "flat", width: 2, height: 2) {
		state("auto", action:"thermostat.off", icon: "st.thermostat.auto")
		state("off", action:"thermostat.cool", icon: "st.thermostat.heating-cooling-off")
		state("cool", action:"thermostat.heat", icon: "st.thermostat.cool")
		state("heat", action:"thermostat.auto", icon: "st.thermostat.heat")
	}

	standardTile("thermostatFanMode", "device.thermostatFanMode", inactiveLabel: true, decoration: "flat", width: 2, height: 2) {
		state "auto", action:"thermostat.fanOn", icon: "st.thermostat.fan-auto"
		state "on", action:"thermostat.fanCirculate", icon: "st.thermostat.fan-on"
		state "circulate", action:"thermostat.fanAuto", icon: "st.thermostat.fan-circulate"
	}

	standardTile("button", "device.switch", width: 2, height: 1, canChangeIcon: true, decoration: "flat") {
			state "off", action: "switch.on", icon: "st.thermostat.heating-cooling-off", backgroundColor: "#ffffff", nextState: "on"
			state "on", action: "switch.off", icon: "st.thermostat.heat", backgroundColor: "#ffffff", nextState: "off"
	}

	standardTile("refresh", "device.thermostatMode", decoration: "flat", width: 1, height: 1) {
		state "default", action:"polling.poll", icon:"st.secondary.refresh"
	}

	standardTile("thermostatOperatingState", "device.thermostatOperatingState", decoration: "flat", width: 2, height: 2) {
		state "idle", action:"polling.poll", label:'${name}', icon: 'http://cdn.device-icons.smartthings.com/sonos/pause-icon@2x.png'
		state "cooling", action:"polling.poll", label:' ', icon: "st.thermostat.cooling"
		state "heating", action:"polling.poll", label:' ', icon: "st.thermostat.heating"
		state "fan only", action:"polling.poll", label:'${name}', icon: "st.Appliances.appliances11"
	}

	valueTile("humidity", "device.humidity", width: 2, height: 2) {
		state "default", label:'${currentValue}%', unit:"Humidity"
	}

	standardTile("temperatureUnit", "device.temperatureUnit", decoration: "flat", width: 2, height: 2) {
		state "fahrenheit",  label: "°F", icon: "st.Weather.weather2", action:"setCelsius"
		state "celsius", label: "°C", icon: "st.Weather.weather2", action:"setFahrenheit"
	}

	main(["temperature", "thermostatOperatingState"])
	details(["temperature", "heatingSetpointUp", "button", "coolingSetpoint", "refresh" /*"button", "thermostatFanMode", "thermostatOperatingState", "refresh", "heat", "water", "heatingSetpoint"*/, "heatingSetpoint", "heat", "water", "heatingSetpointDown" ])
    }
}

// ----------------------------------------------
// Parse ZWave Incomming
// ----------------------------------------------

// parse events into attributes
def parse(String description) {
	 log.debug "Parsing desc => '${description}'"

    def result = null
    def cmd = zwave.parse(description, [0x60:3, 0x25:1, 0x32:1, 0x70:1])
    if (cmd) {
        result = createEvent(zwaveEvent(cmd))
    }
    log.debug "Parsing result => '${result}'"    
    return result
}

//Reports

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
	log.debug "Basic Report"
        [name: "switch", value: cmd.value ? "on" : "off", type: "physical"]
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
	log.debug "Binary Report"
        [name: "switch", value: cmd.value ? "on" : "off", type: "digital"]
}

def zwaveEvent(physicalgraph.zwave.commands.meterv1.MeterReport cmd) {
	log.debug "Meter Report"
	def map = []

	if (cmd.scale == 0) {
    	map = [ name: "energy", value: cmd.scaledMeterValue, unit: "kWh" ]
    }
    else if (cmd.scale == 2) {
    	map = [ name: "power", value: Math.round(cmd.scaledMeterValue), unit: "W" ]
    }

    map
}

def zwaveEvent(int endPoint, physicalgraph.zwave.commands.meterv1.MeterReport cmd) {
	// MeterReport(deltaTime: 1368, meterType: 1, meterValue: [0, 3, 29, 17], precision: 3, previousMeterValue: [0, 3, 29, 17], rateType: 1, reserved02: false, scale: 0, scaledMeterValue: 204.049, scaledPreviousMeterValue: 204.049, size: 4)
	 log.debug "EndPoint $endPoint, MeterReport $cmd"
    def map = []

    if (cmd.scale == 0) {
    	map = [ name: "energy" + endPoint, value: cmd.scaledMeterValue, unit: "kWh" ]
    }
    else if (cmd.scale == 2) {
    	map = [ name: "power" + endPoint, value: Math.round(cmd.scaledMeterValue), unit: "W" ]
    }

    map
}

def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelCmdEncap cmd) {
    log.debug "MultiChannelCmdEncap $cmd"

    def map = [ name: "switch$cmd.sourceEndPoint" ]
    if (cmd.commandClass == 37){
    	if (cmd.parameter == [0]) {
        	map.value = "off"
        }
        if (cmd.parameter == [255]) {
            map.value = "on"
        }
        map
    }
    else if (cmd.commandClass == 50) {
        def hex1 = { n -> String.format("%02X", n) }
        def desc = "command: ${hex1(cmd.commandClass)}${hex1(cmd.command)}, payload: " + cmd.parameter.collect{hex1(it)}.join(" ")
        zwaveEvent(cmd.sourceEndPoint, zwave.parse(desc, [ 0x60:3, 0x25:1, 0x32:1, 0x70:1 ]))
    }
}

def zwaveEvent(physicalgraph.zwave.commands.multichannelv3.MultiChannelCapabilityReport cmd) {
//	  [50, 37, 32], dynamic: false, endPoint: 1, genericDeviceClass: 16, specificDeviceClass: 1)
    log.debug "multichannelv3.MultiChannelCapabilityReport $cmd"
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv1.ConfigurationReport cmd) {
	log.debug "Configuration Report for parameter ${cmd.parameterNumber}: Value is ${cmd.configurationValue}, Size is ${cmd.size}"
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
        // Handles all Z-Wave commands we aren't interested in
        [:]
    log.debug "Capture All $cmd"
}

// handle commands
def refresh() {
	log.debug "Refresh"
	def cmds = []

 	for ( i in 1..4 )
    	cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:i, commandClass:37, command:2).format()

    for ( i in 1..4 ) {
        cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:i, commandClass:50, command:1, parameter:[0]).format()
        cmds << zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:i, commandClass:50, command:1, parameter:[16]).format()
	}

    cmds << zwave.meterV2.meterGet(scale:0).format()
    cmds << zwave.meterV2.meterGet(scale:2).format()

    delayBetween(cmds)
}

// ----------------------------------------------
// Temperature Commands
// ----------------------------------------------

def heatingSetpointUp() {
	log.info "Temp Up"
    state.originalHeatingSetpoint = device.latestValue("heatingSetpoint")
    
    def newHeatingSetpoint
    if (state.originalHeatingSetpoint != null) {
    	newHeatingSetpoint = state.originalHeatingSetpoint + 1
    } else {
    	newHeatingSetpoint = 20
    }
    if (newHeatingSetpoint) {
    	def temp = device.latestValue("temperature")
    	int diff = newHeatingSetpoint - temp
        def cool
        if (diff > 0) cool = "+$diff"
        if (diff == 0) cool = '0'
        if (diff < 0) cool = "$diff"
        sendEvent(name: 'coolingSetpoint', value: cool) 
        sendEvent(name: 'thermostatMode', value: 'heat')
	    sendEvent(name: 'heatingSetpoint', value: newHeatingSetpoint, state: "heat")
        def t = device.latestValue("temperature")
        log.debug t
        if (newHeatingSetpoint <= t) {
        	log.debug "Turning off"
            ChOff()
        } else {
        	log.debug "Turning on"
            ChOn()
        }
    }
}

def heatingSetpointDown() {
	log.info "Temp Down"
    state.originalHeatingSetpoint = device.latestValue("heatingSetpoint")
    
    def newHeatingSetpoint
    if (state.originalHeatingSetpoint != null) {
    	newHeatingSetpoint = state.originalHeatingSetpoint - 1
    } else {
    	newHeatingSetpoint = 20
    }
    if (newHeatingSetpoint) {
    
    	def temp = device.latestValue("temperature")
    	int diff = newHeatingSetpoint - temp
        def cool
        if (diff > 0) cool = "+$diff"
        if (diff == 0) cool = '0'
        if (diff < 0) cool = "$diff"
        sendEvent(name: 'coolingSetpoint', value: cool) 
        sendEvent(name: 'thermostatMode', value: 'heat')
	    sendEvent(name: 'heatingSetpoint', value: newHeatingSetpoint, state: "heat")
     
        def t = device.latestValue("temperature")
        log.debug t
        if (newHeatingSetpoint <= t) {
        	log.debug "Turning off"
            ChOff()
        } else {
        	log.debug "Turning on"
            ChOn()
        }


}
}

def setTemperature(value) {
	log.info value
    sendEvent(name: "temperature", value: value) // works everytime

    log.debug "temperature": value
    log.debug "Setpoint": state.originalHeatingSetpoint

    def sp = state.originalHeatingSetpoint
    int diff = value - sp
    def cool
    if (diff < 0) cool = "$diff"
    if (diff == 0) cool = '0'
    if (diff > 0) cool = "+$diff"
    sendEvent(name: 'coolingSetpoint', value: cool)
    sendEvent(name: 'thermostatMode', value: 'heat')
	sendEvent(name: 'heatingSetpoint', value: device.latestValue("heatingSetpoint"), state: "heat")
    
    def temp = device.latestValue("temperature")
            if (temp >= sp) {
        	log.debug "Turning off"
            ChOff()
        } else {
        	log.debug "Turning on"
            ChOn()
   }   
   }

def setHumidity(value) {
	log.info value
    sendEvent(name: "humidity", value: value) // works everytime
}


def setHeatingSetpoint(value) {
	log.info value
    log.debug "Setpoint received": value
	sendEvent(name: "heatingSetpoint", value: value)
    state.originalHeatingSetpoint = device.latestValue("heatingSetpoint")
    
    def newHeatingSetpoint 
       if (state.originalHeatingSetpoint != null) {
    	newHeatingSetpoint = device.latestValue("heatingSetpoint")
    } else {
    	newHeatingSetpoint = 20
    }
if (newHeatingSetpoint) {
    
    	def temp = device.latestValue("temperature")
    	int diff = newHeatingSetpoint - temp
        def cool
        if (diff > 0) cool = "+$diff"
        if (diff == 0) cool = '0'
        if (diff < 0) cool = "$diff"
        sendEvent(name: 'coolingSetpoint', value: cool) 
        sendEvent(name: 'thermostatMode', value: 'heat')
	    sendEvent(name: 'heatingSetpoint', value: newHeatingSetpoint, state: "heat")
     
        log.debug "Temperature": temp
        if (newHeatingSetpoint <= temp) {
        	log.debug "Turning off"
            ChOff()
        } else {
        	log.debug "Turning on"
            ChOn()
        }
    }   
}

// ----------------------------------------------
// Polling
// ----------------------------------------------

def poll() {
	log.debug "Poll - Refreshing"
	refresh()
}


// ----------------------------------------------
// Button Actions
// ----------------------------------------------

def heatingActive() {
	log.debug "Activate Heating"
	sendEvent(name: "switch", value: "on")
	log.debug "Activate Heating Success"
}

def heatingInactive() {
	log.debug "Deactivate Heating"
	sendEvent(name: "switch", value: "off")
    log.debug "Deactivate Heating Success"
}

def ChOn() {
	log.debug "ChOn"
    sendEvent(name: 'switch1', value: 'on')
    sendEvent(name: 'thermostatMode', value: 'heat')
    sendEvent(name: 'thermostatOperatingState', value: 'heating')
	zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:1, parameter:[255]).format() // 255
}

def ChOff() {
	log.debug "ChOff"
    sendEvent(name: 'switch1', value: 'off')
    sendEvent(name: 'thermostatMode', value: 'off')
    sendEvent(name: 'thermostatOperatingState', value: 'idle')
	zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:1, commandClass:37, command:1, parameter:[0]).format()
}

def HwOn() {
	log.debug "HwOn"
    sendEvent(name: 'switch2', value: 'on')
	zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2, commandClass:37, command:1, parameter:[255]).format()
}

def HwOff() {
	log.debug "HwOff"
    sendEvent(name: 'switch2', value: 'off')
	zwave.multiChannelV3.multiChannelCmdEncap(sourceEndPoint:1, destinationEndPoint:2, commandClass:37, command:1, parameter:[0]).format()
}


def on() {
	ChOn()
	sendEvent(name: "switch", value: "on")
}

def off() {
	ChOff()
	sendEvent(name: "switch", value: "off")
}

365
def setTemperature(value) {
366
    log.info value
367
    sendEvent(name: "temperature", value: value) // works everytime
368
​
369
    log.debug "temperature": value
370
    log.debug "Setpoint": state.originalHeatingSetpoint
371
​
372
    def sp = state.originalHeatingSetpoint
373
    int diff = value - sp
374
    def cool
375
    if (diff < 0) cool = "$diff"
376
    if (diff == 0) cool = '0'
377
    if (diff > 0) cool = "+$diff"
378
    sendEvent(name: 'coolingSetpoint', value: cool)
379
    sendEvent(name: 'thermostatMode', value: 'heat')
380
    sendEvent(name: 'heatingSetpoint', value: device.latestValue("heatingSetpoint"), state: "heat")
381
    
382
    def temp = device.latestValue("temperature")
383
            if (temp >= sp) {
384
            log.debug "Turning off"
385
            ChOff()
386
        } else {
387
            log.debug "Turning on"
388
            ChOn()
389
   }   
390
   }
391
​
392
def setHumidity(value) {
393
    log.info value
394
    sendEvent(name: "humidity", value: value) // works everytime
395
}
396
​
397
​
398
def setHeatingSetpoint(value) {
399
    log.info value
400
    log.debug "Setpoint received": value
401
    sendEvent(name: "heatingSetpoint", value: value)
402
    state.originalHeatingSetpoint = device.latestValue("heatingSetpoint")
403
    
404
    def newHeatingSetpoint 
405
       if (state.originalHeatingSetpoint != null) {
406
        newHeatingSetpoint = device.latestValue("heatingSetpoint")
407
    } else {
408
        newHeatingSetpoint = 20
409
    }
410
if (newHeatingSetpoint) {
411
    
412
        def temp = device.latestValue("temperature")
413
        int diff = newHeatingSetpoint - temp
414
        def cool
415
        if (diff > 0) cool = "+$diff"
416
        if (diff == 0) cool = '0'
417
        if (diff < 0) cool = "$diff"
418
        sendEvent(name: 'coolingSetpoint', value: cool) 
419
        sendEvent(name: 'thermostatMode', value: 'heat')
420
        sendEvent(name: 'heatingSetpoint', value: newHeatingSetpoint, state: "heat")
421
     
422
        log.debug "Temperature": temp
423
        if (newHeatingSetpoint <= temp) {
424
            log.debug "Turning off"
425
            ChOff()
426
        } else {
427
            log.debug "Turning on"
428
            ChOn()
429
        }
430
    }   
431
}
432
​
433
// ----------------------------------------------
434
// Polling
435
// ----------------------------------------------
436
​
437
def poll() {
438
    log.debug "Poll - Refreshing"
439
    refresh()
440
}
441
​
442
​
443
// ----------------------------------------------
444
// Button Actions
445
// ----------------------------------------------
446
​
447
def heatingActive() {
448
    log.debug "Activate Heating"
449
    sendEvent(name: "switch", value: "on")
450
    log.debug "Activate Heating Success"
451
}
452
​
&nbsp;
