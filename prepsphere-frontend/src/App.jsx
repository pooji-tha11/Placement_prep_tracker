import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import Dashboard from "./pages/Dashboard";
import DSA from "./pages/DSA";
import Projects from "./pages/Projects";
import Resumes from "./pages/Resumes";
import Applications from "./pages/Applications";
import Study from "./pages/Study";
import Achievements from "./pages/Achievements";
import Goals from "./pages/Goals";
import Focus from "./pages/Focus";

function App() {
  return (
    <Routes>
      <Route path="/" element={<Layout />}>
        <Route index element={<Dashboard />} />
        <Route path="dsa" element={<DSA />} />
        <Route path="projects" element={<Projects />} />
        <Route path="resumes" element={<Resumes />} />
        <Route path="applications" element={<Applications />} />
        <Route path="study" element={<Study />} />
        <Route path="achievements" element={<Achievements />} />
        <Route path="goals" element={<Goals />} />
        <Route path="focus" element={<Focus />} />
      </Route>
    </Routes>
  );
}

export default App;
