package ma.dentaluxe.mvc.dto.statistiques;

public class BilanFinancierDTO {
    private String periode;
    private double totalRecettesPatients;
    private double totalAutresRevenus;
    private double totalCharges;
    private double resultatNet;
    private long nombreRDV;
    private long nouveauxPatients;

    public BilanFinancierDTO() {}

    public BilanFinancierDTO(String periode, double totalRecettesPatients, double totalAutresRevenus, double totalCharges, double resultatNet, long nombreRDV, long nouveauxPatients) {
        this.periode = periode;
        this.totalRecettesPatients = totalRecettesPatients;
        this.totalAutresRevenus = totalAutresRevenus;
        this.totalCharges = totalCharges;
        this.resultatNet = resultatNet;
        this.nombreRDV = nombreRDV;
        this.nouveauxPatients = nouveauxPatients;
    }

    // Getters et Setters
    public String getPeriode() { return periode; }
    public void setPeriode(String periode) { this.periode = periode; }
    public double getTotalRecettesPatients() { return totalRecettesPatients; }
    public void setTotalRecettesPatients(double totalRecettesPatients) { this.totalRecettesPatients = totalRecettesPatients; }
    public double getTotalAutresRevenus() { return totalAutresRevenus; }
    public void setTotalAutresRevenus(double totalAutresRevenus) { this.totalAutresRevenus = totalAutresRevenus; }
    public double getTotalCharges() { return totalCharges; }
    public void setTotalCharges(double totalCharges) { this.totalCharges = totalCharges; }
    public double getResultatNet() { return resultatNet; }
    public void setResultatNet(double resultatNet) { this.resultatNet = resultatNet; }
    public long getNombreRDV() { return nombreRDV; }
    public void setNombreRDV(long nombreRDV) { this.nombreRDV = nombreRDV; }
    public long getNouveauxPatients() { return nouveauxPatients; }
    public void setNouveauxPatients(long nouveauxPatients) { this.nouveauxPatients = nouveauxPatients; }
}