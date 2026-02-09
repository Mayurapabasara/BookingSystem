import { useEffect, useState } from "react";
import api from "../api/axiosConfig";

function Profile() {
  const [data, setData] = useState("");

  useEffect(() => {
    api.get("/profile")
      .then(res => setData(res.data))
      .catch(() => setData("Unauthorized"));
  }, []);

  return <h2>{data}</h2>;
}

export default Profile;
