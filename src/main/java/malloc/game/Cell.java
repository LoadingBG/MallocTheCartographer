package malloc.game;

public abstract sealed class Cell {
    public static Cell empty() {
        return new Empty(false);
    }

    public static Cell ruins() {
        return new Empty(true);
    }

    public static Cell forest() {
        return new Forest(false);
    }

    public static Cell village() {
        return new Village(false);
    }

    public static Cell farm() {
        return new Farm(false);
    }

    public static Cell water() {
        return new Water(false);
    }

    public static Cell monster() {
        return new Monster(false);
    }

    public static Cell ravine() {
        return new Ravine();
    }

    public static Cell mountain() {
        return new Mountain();
    }

    public final boolean isReplaceable;
    public final boolean hasRuins;

    Cell(boolean isReplaceable, boolean hasRuins) {
        this.isReplaceable = isReplaceable;
        this.hasRuins = hasRuins;
    }

    public abstract Cell withRuins(boolean hasRuins);

    public abstract Cell copy();

    public static final class Empty extends Cell {
        Empty(boolean hasRuins) {
            super(true, hasRuins);
        }

        @Override
        public Cell withRuins(boolean hasRuins) {
            return new Empty(hasRuins);
        }

        @Override
        public Cell copy() {
            return new Empty(hasRuins);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Empty other && hasRuins == other.hasRuins;
        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public String toString() {
            return hasRuins ? "\033[41m \033[0m" : " ";
        }
    }

    public static final class Forest extends Cell {
        Forest(boolean hasRuins) {
            super(false, hasRuins);
        }

        @Override
        public Cell withRuins(boolean hasRuins) {
            return new Forest(hasRuins);
        }

        @Override
        public Cell copy() {
            return new Forest(hasRuins);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Cell.Forest other && hasRuins == other.hasRuins;
        }

        @Override
        public int hashCode() {
            return 11;
        }

        @Override
        public String toString() {
            return hasRuins ? "\033[41m???\033[0m" : "???";
        }
    }

    public static final class Village extends Cell {
        Village(boolean hasRuins) {
            super(false, hasRuins);
        }

        @Override
        public Cell withRuins(boolean hasRuins) {
            return new Village(hasRuins);
        }

        @Override
        public Cell copy() {
            return new Village(hasRuins);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Cell.Village other && hasRuins == other.hasRuins;
        }

        @Override
        public int hashCode() {
            return 21;
        }

        @Override
        public String toString() {
            return hasRuins ? "\033[41m???\033[0m" : "???";
        }
    }

    public static final class Farm extends Cell {
        Farm(boolean hasRuins) {
            super(false, hasRuins);
        }

        @Override
        public Cell withRuins(boolean hasRuins) {
            return new Farm(hasRuins);
        }

        @Override
        public Cell copy() {
            return new Farm(hasRuins);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Cell.Farm other && hasRuins == other.hasRuins;
        }

        @Override
        public int hashCode() {
            return 31;
        }

        @Override
        public String toString() {
            return hasRuins ? "\033[41m???\033[0m" : "???";
        }
    }

    public static final class Water extends Cell {
        Water(boolean hasRuins) {
            super(false, hasRuins);
        }

        @Override
        public Cell withRuins(boolean hasRuins) {
            return new Water(hasRuins);
        }

        @Override
        public Cell copy() {
            return new Water(hasRuins);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Cell.Water other && hasRuins == other.hasRuins;
        }

        @Override
        public int hashCode() {
            return 41;
        }

        @Override
        public String toString() {
            return hasRuins ? "\033[41m~\033[0m" : "~";
        }
    }

    public static final class Monster extends Cell {
        Monster(boolean hasRuins) {
            super(false, hasRuins);
        }

        @Override
        public Cell withRuins(boolean hasRuins) {
            return new Monster(hasRuins);
        }

        @Override
        public Cell copy() {
            return new Monster(hasRuins);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Cell.Monster other && hasRuins == other.hasRuins;
        }

        @Override
        public int hashCode() {
            return 51;
        }

        @Override
        public String toString() {
            return hasRuins ? "\033[41m\uD83D\uDE08\033[0m" : "\uD83D\uDE08";
        }
    }

    public static final class Ravine extends Cell {
        Ravine() {
            super(false, false);
        }

        @Override
        public Cell withRuins(boolean hasRuins) {
            return this;
        }

        @Override
        public Cell copy() {
            return new Ravine();
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Cell.Ravine;
        }

        @Override
        public int hashCode() {
            return 61;
        }

        @Override
        public String toString() {
            return "???";
        }
    }

    public static final class Mountain extends Cell {
        private boolean hasCoin;

        Mountain() {
            this(true);
        }

        private Mountain(final boolean hasCoin) {
            super(false, false);
            this.hasCoin = hasCoin;
        }

        @Override
        public Cell withRuins(boolean hasRuins) {
            return this;
        }

        @Override
        public Cell copy() {
            return new Mountain(hasCoin);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Cell.Mountain other && hasCoin == other.hasCoin;
        }

        @Override
        public int hashCode() {
            return 71;
        }

        public boolean hasCoin() {
            return hasCoin;
        }

        public void collectCoin() {
            hasCoin = false;
        }

        @Override
        public String toString() {
            return hasCoin ? "M" : "m";
        }
    }
}
