import lib.tvwzEngine.graphics.interfaces.Renderable;
import lib.tvwzEngine.graphics.interfaces.Updatable;
import lib.tvwzEngine.graphics.simple.Quad;
import lib.tvwzEngine.math.Vector3;

public class Person implements Updatable, Renderable {

    public static float MAX_INFECTION_CHANCE = 0.9f;
    public static Vector3 infectedColor = new Vector3(150, 0, 0),
            vaccinatedColor = new Vector3(0, 0, 150),
    recoveredColor = Vector3.white(),
    susceptibleColor = new Vector3(0, 50, 0);


    public float transmissionChance;
    public float infectiousPeriod;
    public float timeSpentInfected;
    public InfectionState infectionState;

    public Quad quad;

    public Person (float baseInfPeriod, float infRandomness, float transmissionChance, float transRandomness) {
        this.infectionState = InfectionState.SUSCEPTIBLE;
        this.infectiousPeriod = baseInfPeriod + (float) (infRandomness * Math.random() * 2);
        timeSpentInfected = 0;
        this.transmissionChance = transmissionChance;//Math.min(transmissionChance + (float) (transRandomness * Math
        // .random() * 2),
        //MAX_INFECTION_CHANCE);

    }

    public boolean tryInfect (float infectionChance) {

        if (infectionState != InfectionState.SUSCEPTIBLE) return false;

        if (Math.random() < infectionChance) {
            infectionState = InfectionState.INFECTED;
            timeSpentInfected = 0;
            return true;
        }
        return false;
    }

    @Override
    public void render (float startDepth) {
        Vector3 color = Vector3.white();
        switch (infectionState) {
            case INFECTED:
                color = infectedColor;
                break;
            case SUSCEPTIBLE:
                color = susceptibleColor;
                break;
            case VACCINATED:
                color = vaccinatedColor;
                break;
            case RECOVERED:
                color = recoveredColor;
                break;
        }

        quad.setColor(color);
        quad.render(startDepth);

    }

    @Override
    public void update () {
        if (infectionState == InfectionState.INFECTED) {
            timeSpentInfected++;
            if (timeSpentInfected >= infectiousPeriod) {
                timeSpentInfected = 0;
                infectionState = InfectionState.RECOVERED;
            }
        }
    }
}

