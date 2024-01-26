package twilightforest.util;

public enum ComponentAlignment {
    LEFT {
        @Override
        public int getTextOffset(int textWidth, int maxColumnWidth) {
            return 0;
        }
    },
    CENTER {
        @Override
        public int getTextOffset(int textWidth, int maxColumnWidth) {
            return (maxColumnWidth - textWidth) >> 1;
        }
    },
    RIGHT {
        @Override
        public int getTextOffset(int textWidth, int maxColumnWidth) {
            return maxColumnWidth - textWidth;
        }
    };

    public abstract int getTextOffset(int textWidth, int maxColWidth);
}
