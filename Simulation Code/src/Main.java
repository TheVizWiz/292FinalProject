import lib.tvwzEngine.Window;
import lib.tvwzEngine.graphics.interfaces.Renderable;
import lib.tvwzEngine.graphics.interfaces.RenderableBase;
import lib.tvwzEngine.graphics.interfaces.Updatable;
import lib.tvwzEngine.graphics.simple.Quad;
import lib.tvwzEngine.input.Input;
import lib.tvwzEngine.math.Time;
import lib.tvwzEngine.math.Vector3;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class Main extends RenderableBase implements Updatable {

    public static Window window;

    Person[][] population;
    int popsize;
    float vaccinationRate;
    int numDays = 0;
    int daysPerYear = 100;



    public static void main (String[] args) {
        GLFW.glfwInit();

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        int size = (int) (Math.min(vidMode.width(), vidMode.height()) / 1.1);
        window = new Window(size, size, "OpenGL Simulation");//
        window.setBackgroundColor(new Vector3(10, 10, 10));
        window.setResizable(false);
        window.create();
        window.setAntiAlias(true, true);
        Input.setGLFWCallBacks(window);
        window.framesPerUpdate = 1;


        Main main = new Main(size , 300, 5);


        window.updatableList.add(main);
        window.renderableList.add(main);


        while (true) {

            window.step();
            if (window.shouldClose()) {
                window.destroy();
                glfwTerminate();
                Input.terminate();
                return;
            }
        }
    }

    public Main (float windowSize, int size, int numInfected) {
        population = new Person[size][size];
        depth = -1;
        float squareWidth = windowSize / (size + 2);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                population[i][j] = new Person(14, 3, 0.01f, 0.0f);
                population[i][j].quad = new Quad(squareWidth * i + i, squareWidth * j + 1, squareWidth, squareWidth);
            }
        }
        popsize = size * size;
        vaccinationRate = 0.005f;
        infectRandom(numInfected);
    }

    public void tickPop (Person[][] pop) {
        for (Person[] people : pop) {
            for (Person person : people) {
                person.update();
            }
        }
    }


    public void countState () {
        int infected = 0, recovered = 0, susceptible = 0, vaccinated = 0;
        for (Person[] people : population) {
            for (Person person : people) {
                switch (person.infectionState) {
                    case INFECTED -> infected++;
                    case RECOVERED -> recovered++;
                    case VACCINATED -> vaccinated++;
                    case SUSCEPTIBLE -> susceptible++;
                }
            }
        }
        System.out.println(susceptible + "," + infected + "," + recovered + "," + vaccinated);
    }

    public void swap (int numSwaps) {
        int size = population.length;

        for (int i = 0; i < numSwaps; i++) {
            int x2 = (int) (Math.random() * size);
            int x1 = (int) (Math.random() * size);
            int y1 = (int) (Math.random() * size);
            int y2 = (int) (Math.random() * size);
            Person person = population[x1][y1];
            population[x1][y1] = population[x2][y2];
            population[x2][y2] = person;
        }
    }

    public void infectRandom (int numInfections) {
        int size = population.length;

        for (int i = 0; i < numInfections; i++) {
            int x = (int) (Math.random() * size);
            int y = (int) (Math.random() * size);
            population[x][y].infectionState = InfectionState.INFECTED;
        }
    }


    public void vaccinate (int numVaccines) {
        int numVaccinated = 0;

        for (Person[] people : population) {
            for (int i = 0; i < population.length; i++) {
                if (people[i].infectionState == InfectionState.SUSCEPTIBLE) {
                    numVaccinated++;
                    people[i].infectionState = InfectionState.VACCINATED;
                    if (numVaccinated >= numVaccines) return;
                }
            }
        }
    }


    @Override
    public void render (float startDepth) {
        for (Person[] people : population) {
            for (Person person : people) {
                person.render(startDepth + depth);
            }
        }
    }

    @Override
    public void update () {

        numDays++;
        window.setTitle("OpenGL Simulation - year " + (numDays / daysPerYear) + ", day " + (numDays % daysPerYear));
        tickPop(population);
        swap(population.length * population.length / 2);

        for (int i = 1; i < population.length - 1; i++) {
            for (int j = 1; j < population.length - 1; j++) {

                if (population[i][j].infectionState == InfectionState.SUSCEPTIBLE) {
                    outerloop:
                    for (int x = -1; x <= 1; x++) {
                        for (int y = -1; y <= 1; y++) {
                            if (x == y && x == 0) continue;
                            if (population[i + x][j + y].infectionState == InfectionState.INFECTED) {
                                if (population[i][j].tryInfect(population[i + x][j + y].transmissionChance * (numDays % daysPerYear > 2 * daysPerYear / 3 ? 2f : 1))) {
                                    break outerloop;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (Time.deltaTime() > 2) vaccinationRate = 0.00001f;
        vaccinate((int) (popsize *  vaccinationRate  * Time.deltaTime() * Math.max(Time.totalTime() / 10 - 1, 0)));
        countState();
    }
}
