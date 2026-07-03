import java.util.Objects;

public class State {

    int i;
    int j;

    State(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return i == state.i && j == state.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
}
