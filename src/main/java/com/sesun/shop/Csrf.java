package com.sesun.shop;

public class Csrf {
    /*

    csrf 킬 때 복붙

    http.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository())
                .ignoringRequestMatchers("/login")
        );


    csrf 끌 때 복붙

    http.csrf((csrf) -> csrf.disable());


    html에는 form 태그마다 이거 복붙

    <input type = "hidden"
               th:name = "${_csrf.parameterName}"
               th:value = "${_csrf.token}">

     */
}
