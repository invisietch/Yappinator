module.exports = {
  content: [
    "./public/**/*.html",
    "./src/**/*.cljs",
  ],
  theme: {
    extend: {
      colors: {
        gruvbox: {
          dark0: "#282828",
          dark1: "#3c3836",
          dark2: "#504945",
          dark3: "#665c54",
          dark4: "#7c6f64",
          gray: "#928374",
          fg: "#ebdbb2",
          red: "#fb4934",
          green: "#b8bb26",
          yellow: "#fabd2f",
          blue: "#83a598",
          purple: "#d3869b",
          aqua: "#8ec07c",
          orange: "#fe8019",
        },
      },
    },
  },
  plugins: [],
};
