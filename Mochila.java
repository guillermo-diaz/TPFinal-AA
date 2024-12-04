import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Mochila {

    static int[] pesos   = {2, 3, 4, 5, 6}; 
    static int[] valores = {3, 4, 5, 8, 9}; 
    static int capacidad = 10; // Capacidad máxima (mochila)
    static int tamPoblación = 6; // Tamaño de la población
    static double tasaCruce = 0.9; // Probabilidad de cruce
    static double tasaMutación = 0.2; // Probabilidad de mutación
    static int generacionesMax = 50; // Máximo número de generaciones

    public static void main(String[] args) {
        List<Individuo> población = generarPoblaciónInicial();
        Individuo mejorIndividuo;
        int generacion = 0;

        while (generacion < generacionesMax) {
            System.out.println("Generación " + generacion);
            imprimirPoblación(población);

            // Cruzar
            if (Math.random() < tasaCruce) {
                Individuo[] mejores = seleccionarDosMejores(población);
                Individuo padre1 = mejores[0];
                Individuo padre2 = mejores[1];
                Individuo[] hijos = cruzar(padre1, padre2);
                borrarPeores(población);
                población.add(hijos[0]);
                población.add(hijos[1]);
            }

            mejorIndividuo = obtenerMejorIndividuo(población);

            // Mutación
            for (Individuo individuo : población) {
                if (Math.random() < tasaMutación && mejorIndividuo != individuo) { //si no es el mejor individuo, muta
                    individuo.mutar();
                }
            }
            generacion++;
        }

        // Obtener y mostrar el mejor individuo
        mejorIndividuo = obtenerMejorIndividuo(población);
        System.out.println("\nMejor solución:");
        System.out.println(mejorIndividuo);
    }

    public static Individuo[] cruzar(Individuo padre1, Individuo padre2) {
        int puntoCruce = pesos.length/2; //se divide el arreglo a la mitad
        int[] hijo1 = new int[pesos.length];
        int[] hijo2 = new int[pesos.length];

        for (int i = 0; i < pesos.length; i++) {
            if (i <= puntoCruce) {
                hijo1[i] = padre1.objetos[i];
                hijo2[i] = padre2.objetos[i];
            } else {
                hijo1[i] = padre2.objetos[i];
                hijo2[i] = padre1.objetos[i];
            }
        }
        return new Individuo[]{new Individuo(hijo1, valores, pesos, capacidad), new Individuo(hijo2, valores, pesos, capacidad)};
    }

    
    public static List<Individuo> generarPoblaciónInicial() {
        List<Individuo> población = new ArrayList<>();
        for (int i = 0; i < tamPoblación; i++) {
            población.add(generarIndividuoAleatorio());
        }
        return población;
    }

    public static Individuo generarIndividuoAleatorio() {
        int[] objetos = new int[pesos.length];
        Random random = new Random();

        for (int i = 0; i < pesos.length; i++) {
            objetos[i] = random.nextBoolean() ? 1 : 0;
        }
        return new Individuo(objetos, valores, pesos, capacidad);
    }

    public static void borrarPeores(List<Individuo> población) {
    
        Individuo peor = null;
        Individuo segundoPeor = null;
    
        for (Individuo ind : población) {
            if (peor == null || ind.fitness < peor.fitness) {
                // Actualizamos el segundo peor antes de actualizar el peor
                segundoPeor = peor;
                peor = ind;
            } else if (segundoPeor == null || ind.fitness < segundoPeor.fitness) {
                // Actualizamos solo el segundo peor si no es peor que el actual peor
                segundoPeor = ind;
            }
        }
    
        // Eliminamos los dos peores individuos
        población.remove(peor);
        población.remove(segundoPeor);
    }

    public static Individuo[] seleccionarDosMejores(List<Individuo> población) {

        Individuo mejor = null;
        Individuo segundoMejor = null;
    
        for (Individuo ind : población) {
            if (mejor == null || ind.fitness > mejor.fitness) {
                // Actualizamos el segundo mejor antes de actualizar el mejor
                segundoMejor = mejor;
                mejor = ind;
            } else if (segundoMejor == null || ind.fitness > segundoMejor.fitness) {
                // Actualizamos solo el segundo mejor si no supera al mejor
                segundoMejor = ind;
            }
        }

        return new Individuo[]{mejor, segundoMejor};
    }

    public static Individuo obtenerMejorIndividuo(List<Individuo> población) {
        Individuo mejorIndividuo = null;
        for (Individuo ind : población) {
            if (mejorIndividuo == null || ind.fitness > mejorIndividuo.fitness) {
                mejorIndividuo = ind;
            }
        }
    return mejorIndividuo;
    }

    public static void imprimirPoblación(List<Individuo> población) {
        for (Individuo individuo : población) {
            System.out.println(individuo);
        }
    }
}

class Individuo {
    public int[] objetos, valores, pesos;
    public int valorTotal;
    public int pesoTotal;
    public int fitness;
    private int capacidad;

    public Individuo(int[] objetos, int[] valores, int[] pesos, int capacidad) {
        this.objetos = objetos;
        this.valores = valores;
        this.pesos = pesos;
        this.capacidad = capacidad;
        calcularFitness();
    }

    private void calcularFitness() {
        valorTotal = 0;
        pesoTotal = 0;
        for (int i = 0; i < objetos.length; i++) {
            if (objetos[i] == 1) {
                valorTotal += valores[i];
                pesoTotal += pesos[i];
            }
        }

        fitness = (pesoTotal <= capacidad) ? valorTotal : 0; // si excede el peso se le asigna un valor 0 de fitness
    }

    public void mutar() {
        Random random = new Random();
        int indice = random.nextInt(objetos.length);
        objetos[indice] = 1 - objetos[indice]; 
        if (objetos[indice] == 1){
            pesoTotal += pesos[indice];
            valorTotal += valores[indice];
            fitness = (pesoTotal <= capacidad) ? valorTotal : 0; // si excede el peso se le asigna un valor 0 de fitness
        }
    }

    @Override
    public String toString(){
        return Arrays.toString(objetos) +
                " - Peso: " + pesoTotal +
                ", Valor: " + valorTotal +
                ", Fitness: " + fitness;
    }
}



