/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        cream: "#FAF6EF",
        surface: "#FFFFFF",
        surfaceAlt: "#F3ECE1",
        ink: "#4A3F35",
        inkMuted: "#8A7B6C",
        plum: "#8E6FA8",
        plumDark: "#6F5286",
        clay: "#C08552",
        clayLight: "#E8CBAE",
        success: "#9CB68A",
        danger: "#D98E82",
        border: "#E6DCCB",
      }
    },
  },
  plugins: [],
}
