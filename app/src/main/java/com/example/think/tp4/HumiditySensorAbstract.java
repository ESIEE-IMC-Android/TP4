package com.example.think.tp4;


public abstract class HumiditySensorAbstract{
	public abstract float value() throws Exception;
	
	public abstract long minimalPeriod();
}
