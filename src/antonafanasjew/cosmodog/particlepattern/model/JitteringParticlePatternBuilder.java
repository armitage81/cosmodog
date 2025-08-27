package antonafanasjew.cosmodog.particlepattern.model;

import antonafanasjew.cosmodog.topology.Rectangle;

import java.util.Collection;
import java.util.Random;

public class JitteringParticlePatternBuilder implements ParticlePatternBuilder {

    private Random random = new Random(1);
    private ParticlePatternBuilder delegate;
    private float jitteringHorizontal;
    private float jitteringVertical;

    public static JitteringParticlePatternBuilder instance(
            ParticlePatternBuilder delegate,
            float jitteringHorizontal,
            float jitteringVertical) {
        JitteringParticlePatternBuilder instance = new JitteringParticlePatternBuilder();
        instance.delegate = delegate;
        instance.jitteringHorizontal = jitteringHorizontal;
        instance.jitteringVertical = jitteringVertical;
        return instance;
    }

    @Override
    public ParticlePattern build(Rectangle surface) {
        ParticlePattern retVal = new ParticlePattern(surface);
        ParticlePattern particlePattern = delegate.build(surface);
        Collection<Particle> particles = particlePattern.getParticles().values();
        for (Particle particle : particles) {
            float horizontalOffset = particle.getOffset().getX();
            float verticalOffset = particle.getOffset().getY();
            int horizontalJitteringOffset;
            if (jitteringHorizontal < 0) {
                horizontalJitteringOffset = random.nextInt((int)-jitteringHorizontal);
            } else {
                horizontalJitteringOffset = random.nextInt((int)jitteringHorizontal);
            }

            int verticalJitteringOffset;
            if (jitteringVertical < 0) {
                verticalJitteringOffset = random.nextInt((int)-jitteringVertical);
            } else {
                verticalJitteringOffset = random.nextInt((int)jitteringVertical);
            }
            Particle jitteringParticle = new Particle(
                    horizontalOffset + horizontalJitteringOffset,
                    verticalOffset + verticalJitteringOffset
            );
            retVal.addParticle(jitteringParticle);
        }
        return retVal;
    }
}
