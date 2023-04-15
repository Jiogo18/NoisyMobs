package com.jiogo18.noisymobs.engine;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

public class SoundLimiter {
	private final int maxSoundCountPerCycle;
	private final int cycleDuration; // seconds
	private final Random random = new Random();
	private int soundCountPlayed = 0;
	private int soundCountTotal = 0;
	private float previousProportionPlayed = 1.0f;
	private long startNextCycle = 0;

	public SoundLimiter(int maxSoundCountPerSecond, int cycleDuration) {
		this.maxSoundCountPerCycle = maxSoundCountPerSecond;
		this.cycleDuration = cycleDuration;
	}

	protected void resetCycle() {
		previousProportionPlayed = (soundCountTotal == 0 || soundCountPlayed == 0) ? 1.0f
				: (float) soundCountPlayed / soundCountTotal;

		soundCountPlayed = 0;
		soundCountTotal = 0;
		startNextCycle = System.currentTimeMillis() + cycleDuration * 1000;
	}

	public boolean canPlayNewSound(Vector3d position) {
		// if too far away, play the sound but don't count it
		Minecraft instance = Minecraft.getInstance();
		ClientPlayerEntity player = instance.player;
		if (player == null)
			return true; // server
		Vector3d clientPosition = player.position();
		double distanceSquared = clientPosition.distanceToSqr(position);
		if (distanceSquared > 256)
			return true;

		soundCountTotal++;

		// too many sounds played for this cycle
		if (maxSoundCountPerCycle <= soundCountPlayed)
			return false;

		// if there was a lot of sounds not played in the previous cycle, reduce the
		// chance to play the sound
		if (previousProportionPlayed <= this.random.nextFloat())
			return false;

		// calculate the probability of playing a new sound, based on the number of
		// sounds played and the time left
		int soundCountLeft = maxSoundCountPerCycle - soundCountPlayed;
		float soundCountLeftProportion = (float) soundCountLeft / maxSoundCountPerCycle;
		long durationLeft = startNextCycle - System.currentTimeMillis();
		if (durationLeft <= 0) {
			return true;
		}
		float durationLeftProportion = (float) durationLeft / (cycleDuration * 1000);

		float leftProp = soundCountLeftProportion / durationLeftProportion;

		if (leftProp < 2)
			return false;

		soundCountPlayed++;
		return true;
	}
}
