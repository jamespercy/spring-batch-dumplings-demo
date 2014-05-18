package batch.domain;

public class BoxOfDumplings {
    private DumplingType type;
    private int dumplings;

    public BoxOfDumplings() {
    }

    public BoxOfDumplings(DumplingType type, int dumplings) {
        this.type = type;
        this.dumplings = dumplings;
    }

    public int getDumplings() {
        return dumplings;
    }

    public DumplingType getType() {
        return type;
    }

    public void setType(DumplingType type) {
        this.type = type;
    }

    public void setDumplings(int dumplings) {
        this.dumplings = dumplings;
    }


    @Override
    public String toString() {
        return String.format(
                "BoxOfDumplings[type='%s', dumplings='%s']",
                type, dumplings);
    }
}
