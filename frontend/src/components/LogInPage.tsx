import React from 'react'
import { Button } from "@nextui-org/react";

const LogInPage = () => {
    const loginWith = (provider: String) => {
        window.location.href = `http://localhost:8080/oauth2/authorization/${provider}`;
    }

  return (
    <div>
        <Button onPress={() => loginWith("google")}>Google</Button>
        <Button onPress={() => loginWith("github")}>Github</Button>
        <Button onPress={() => loginWith("naver")}>Naver</Button>
    </div>
  )
}

export default LogInPage
