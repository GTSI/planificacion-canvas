package business_services.celexcsv.data;

public class CourseCelexCSVData {
    public String nombre_curso;
    public String paralelo;
    public String codigo_materia;
    public String cedula1;
    public String cedula2;
    public String cedula3;

    public CourseCelexCSVData(String nombre_curso, String paralelo, String codigo_materia, String cedula1, String cedula2, String cedula3) {
        this.nombre_curso = nombre_curso;
        this.paralelo = paralelo;
        this.codigo_materia = codigo_materia;
        this.cedula1 = cedula1;
        this.cedula2 = cedula2;
        this.cedula3 = cedula3;
    }
}


