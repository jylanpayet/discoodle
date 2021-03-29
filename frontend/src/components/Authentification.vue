<template>
   <div class="Authentification">
      <div class="content">
         <div class="login" v-if="showLogin">
            <div style="display: flex; flex-direction: column; align-items: flex-start; justify-content: center; width: 100%;">
               <span style="color: #F4F4F4; font-size: 30px; font-weight: 600">Se connecter</span>
               <input type="text" placeholder="Pseudo" name="userlog" autocomplete="false" required spellcheck="false" @keypress="actionInputLogin">
               <input type="password" placeholder="Mot de passe" name="passwordlog" required spellcheck="false" @keypress="actionInputLogin">
               <a href="" style="font-size: 12px; color: #909090">Mot de passe oublié ?</a>
               <label>
                  <input type="checkbox">
                  Se souvenir de moi !
               </label>
               <button @click="login" style="align-self: center; margin-top: 15px;" class="submit">C'est parti !</button>

               <button @click="showLogin = false" style="margin-top: 10px; border: none; outline: none; background: none; cursor: pointer; color: #F4F4F4; font-size: 12px; font-weight: 600; align-self: center">Inconnu au bataillon ? Inscrivez-vous !</button>
            </div>
         </div>
         <div class="register" v-else>
            <span style="margin-bottom: 15px; color: #F4F4F4; font-size: 30px; font-weight: 600">Créer mon compte</span>

            <input type="text" placeholder="Pseudo" name="username" required @keypress="actionInputRegister">
            <input type="text" placeholder="Prénom" name="name" required @keypress="actionInputRegister">
            <input type="text" placeholder="Nom" name="lastname" required @keypress="actionInputRegister">
            <input type="email" placeholder="Mail" name="mailReg" required @keypress="actionInputRegister">
            <input type="password" placeholder="Mot de passe" name="passwordReg" required @keypress="actionInputRegister">
            <button @click="userRegistration" style="align-self: center; margin-top: 15px;" class="submit">C'est parti !</button>


            <button @click="showLogin = true" style="margin-top: 10px; border: none; outline: none; background: none; cursor: pointer; color: #F4F4F4; font-size: 12px; font-weight: 600; align-self: center">Déjà un compte ? Connectez-vous !</button>

         </div>
         <div class="about">
            <svg style="margin-bottom: 10px;" viewBox="0 0 347 108" fill="none" xmlns="http://www.w3.org/2000/svg"
                 xmlns:xlink="http://www.w3.org/1999/xlink">
               <rect width="347" height="108" fill="url(#pattern0)"/>
               <defs>
                  <pattern id="pattern0" patternContentUnits="objectBoundingBox" width="1" height="1">
                     <use xlink:href="#image0" transform="translate(0 -0.0205) scale(0.002 0.00642593)"/>
                  </pattern>
                  <image id="image0" width="500" height="162"
                         xlink:href="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAfQAAACiCAYAAAC3dj/UAAAgAElEQVR4Ae2dCZwcRb3Ho094T+WJooj4EERBNEJIumY34Q4gl4pcxoByQxKSsLvT1bObAzBDkp2u6tkkQIgSBAGRxzM85JDnAz9yKaBIHhiSzU5XzewGAiEc4RLkpt+nJtvJ7GR6prvnHn77+cynt7urq/7/bx2/PuoYNQp/IAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACIAACTUhgzJgxn25vb98zEom0RyKRMwghswghizRN+z0hZCUh5GVCyBuRSOTUJnQPJoMACIAACIBA6xDYb7/9Pjdu3LjRkUjkoEgkch4hZD4h5PZIJPIYISRDCHmLEOJ4/Fa1tbUd2Do04AkIgAAIgAAINDiB0aNH76Bp2rc0TZtMCLk4Eoks1zTtYULIeg+x9hJx9/jVBx100L83uNswDwRAAARAAASal8C4ceO+rGnaBELIjGHh/hshZIgQ8n5I8XZFXG2f1zTt9OalA8tBAARAAARAoAEJjB49entCyDcJIecTQpYRQh4lhGyqgHDnirj7/70qrQbEAJNAAARAAARAoLkITJw48ROapo3RNG0KIWQFIcQmhLxbJQF3hfxDTdMWTpo06V+aixasBQEQAAEQAIEGIjB+/Phd1LdvTdOuIoSsJoR8WGUBd4VcbTORSOSEBsIBU0AABEAABECgeQiMHTt2b03TphJC7iKEvFBDAd8i5pqm3bH//vv/R/NQg6UgAAIgAAIg0AAExo8fvxsh5IJhEf9nPUR8OE2V9qxRo0Z9rAGwwAQQAAEQAAEQaHwCavKWSCRyCiHkd8MTtWx5Qq6ToA+oMemNTw4WggAIgAAIgEADEIhEIvsSQhghJFUn4S5043DDuHHjdm4APDABBEAABEAABBqXgOolrmna9wkhNxFC3m4gIX9Ffa9vXHKwDARAAARAAAQagAAh5AuRSETXNE1N8FLoybhexz4ghDzU1tb27QbABBNAAARAAARAoDEJEEJ21zRtHiHkmQYTcvcGQnV+O7gx6cEqEAABEAABEKgzAULI1wkhlxFCXmpQIXcFXW2f0zTtmDojQ/IgAAIgAAIg0DgEcoT89SYQ8lxRf0/TtGjjkIQlIAACIAACVScwaUX/9kZCjjZY+riOJf1n66boNlh6AbVEknKZoFxcorNMFzXtyZSnDu+25NerblTIBDoScmejz26LMXl8tFecrVuiy2BydozLOZ29gzM7E+mzYknxPZ3Jb8eSqz7tlUxbW9vXCCGXE0JeaTIhzxV1JxKJWF4+1ut4PO58XDfF12YxcejZv1h38sm3bDx/9gJbN5icS7mco5tSn5UYnEItcYpuycNUWHVNvext1nRnLOvfgTJxnc7FXZSLO/z+suGZuK6rd3CXavveyTK7UyafNJh4n3LhhPi9S7l4p8yfSvf1avv6kY5fZ/J4ncvf+y2EwcLJ2wwubqaWuIpyOa/bHDh76pUbx3deltk9Hr//E9UC30gVrIfL3ahlT9aTmRujycxqg4uXVGUymCxVqT6g3H6OcvlY1BI/m70g8514vH+najErFa++eP1O5/984xEGs3ujZvpPlIsM5eINHw3DJsrF2hgbur0rkTpv5sK1e6i0CCG7EkJMQsirzSzkebb/ghCyXSmW1TqvxJgmMt+gljyXsqGbDCb/vrkMiXdjpu309NqObnk25u9tDmv/nVqpm6KmPPfCxNA3J61wKjoHfUdcfibK5X9RLn4XrC3xLZQrDC6up5bgsaTo1C15NE0MfGMWy+xYLe6qblAuXvVRFwrV+Vd1M71XtWxz482WCybfMZhn/heyrRrH3nZtwrYKBKglYiELYsjMttV1qpH/A00KZvSJQyvdaDRCBTNY5mDKUtdSLp7fLOC2Q1nW95DchBMz7f4f/ubp+XTRpq9UoSgUjDJqDY2lXC5SolxeOdnse+fCVa8e/6M5KyPtE+oyJWueAI94yq7QuZsOOOCATxaEWaWDqrzrXM6g3P5fysVbm/OpvLI2nNfv0KS8W+fpC9UbmUqY35F4fGfKxAfllaXAovS+wcUTXWbmOsrSZxp99hcq4Ysbx2zzqc9RLjaE9GmDejPixlWtrbppoEy+2QCC/ma1fES8o0aNMizRFbIghhamvPTeptx+vGfhhs4L+dNfrkSm1LOCTbticH+D2/9JuXg7z8+K8OpZmHIMJjZ2s2cWzKlQI1uIedQSY3Vm3+jzKdyHb7bz4wuudg45/IRqiGjDxDlWyw6tu7oWq6vN7V23azQp51Eunq5GWcuL81mDpRaoN06FyovfY1Pj9hcoE6/nxe2j/AQW8WJxbtS5vawrIcb7tbtYuHq2N8Xsyj0HQc+l0cL/N4CgD1c89TowtU5PprpnxPt3KAd5PSrY1OUrtzv72sG5XUn5Wm0aK9sxzAH7gp8/f1I5rPKv7bhCfqZj8TqLWrJije459FbniGN/0jCiW6En8YL+RNraPzzi2FMfOvqMmGe/gXzmQffVG60YE9MMJoZqU9ZyxNQSTxlWqkuV96B2q/ANIuiu2Kub7l/HkoNjwvjiXlOP9sZN2+8Wgu6XVJOHaxxBH9Fo/C2aSIeeE7vWFUx1aqGWuEN9ozTCdThxG5gw2w91Jnsr0Zlp6mXrx0eTg48ZZiqMHdtcM2X2Pc4xP5hWUPiqKar1invfsRHn2BNnOtPm3usYiYG7z/3Zi/tUunkw+tbuoSft22su5Hnl2kikQvnXYIK+ucwy8Vo3E7GwdajW7U2YMgVBD0OtCa9pSEHf3GnsjY4la6eHQVrLCtbB5W4GTz1S7wZW5/bN6uk6DC91TZSnfkhZahPlFfn2mm0oDzvyZGf8AYc5kbZ2Z/yEQ5wJBx5e4DfRmXDgRGfCAROd9vaD3iYksml4DLravjz8Uz3g1U91oHuvXoJdLN39x0WcQyYe75wXu3PLjU00mV5n8MGjwuZJ/nWGabdRJvrrXdbc9MP415CCPnyzovPMijmJRz+fz73Ufi3bm1K2eJ2HoHuRabHjjSro2UZD9chMynhQ5LWqYKpzjcFF3cXcbWApE/fMSQwEbpB0Zp9ajW/+Xb1POl29q50jjj3dOde43elauGqbX+fCvzvZ34InnM4FTzjT44/decgxp+/a3t7+ebXYifq1tbV9Sf2Ge8bfUkxY63FunEacAw460pl8/hXOyBui7M3RP/SkPD5oGc4PH0vKie4IiS35nffUXPvjwf1rZEGnXDpzFvT/9Zxfrg7U6bRW7U1+mQiyD0EPQquJwza0oGcbLNvp4WkjCOJaVbAYs2+ofSOa82miQINucHFXkCf1bjN1LOXizWr5obMB5zvfPcuZOe+RLU+updIymLjMK78JIVfXQ7S90tTIZjH/4dlJp2P+/xX0UbfE63N7M0d6+VTquOqgODz8rGD8pXhW+3wQ/xpb0IUTSwhn2rLBP7U/+rrvG+NatTelykmx8xD0YnRa6FzjC7oSMPlOtzl0rF/stahghpn6SbUbyrDxq3G4fr4HquEylKeeCpuOn+s6FjzuHH7MaU7H/JW+xcjgttPFMj8ulN+apl3rJa61Pp4r5p0LHi/qn8HSQ2EmCorHH9lJ55kn/LCuZxi//jW6oCuGhimcOZemfjXKcT5WqAzmH6tFe5OfZtB9CHpQYk0avjkEXYl6amiuzxmVql3BPvOas1N3QqTr2YAWSXsTTdpT/Iztp1zeViSeogLl97qZ8b866lu6eq3u95psOFNu7CgwU56maVfVWrgLpee+ZldP5qXE3PVbzQwWtJnoNtcsV6+C3TgaeevHv2YQ9C2Mk/YUP/lV7fbGjw2lwkDQSxFqkfPNI+jCoSyd9IO92hWsY5GcvqXSF3jtXa9z3WZqVVef3eaHEeXyB5RXf4KP6Zf8OdtZLJpYHUiU1AQYXUvkDfm+aJrGCwlsLY+NHRfJfjMPIuZumTBMcVq+T177atIlncuwU3UG4u3aV+62lH/NJOg6l0/H46u+6JU/7vFqtzduOuVsIejl0Guia5tK0LnY1Gmt37sU3mpWMPXkS5n4S7kN3/D1acrlQzEu/ody8UfKhTtVZ6DGWInf9Mszv5x4/yufLcVGnVfT7qp0K+RDUVunX/ygc+iRJzm6ubZouEK2RJP2B2dd89KIb8+EkFgtxTs/rf3GRZyDDj3OmXTOIt9P5iN8Y/LJqfENnyqVT+qTic5tVS4Cc6vrNSX8ayZBVxwNJi4ulVfVbG9Kpe33fAMJ+lt+bUa4EASaTNAdmkxfVMrNalawGUv6xxpM+pnD3LMhVh3XDGYfV6jzWiyZ2pOa4hTdTP+KcvmGXvp165vdbKCjFJPc80afOKpWjf4FFz/gTDzqR47qHBc8TduZs2Do3qnLnS2TmGiadk6+yNZsPxJxjv7++c7Z+i1O0DcOru+GZTsdZqbkU3osIcZXcuSBPvLG4EPKhfqFyJPi15Tyr9kEnXKRLlRPc+tTNdub3HTK+b8Cgq4WZlHTCof9qUl8VJnD4izlZGSpa5tO0LlYHY87RRd2qWYF67x8zVk+FlYp2FCqJ+nZC1LMT4c1lW9zezfuG2OrrvF6Na5bUsxesOqIUnmcf55y++pKNeZqjmydi19Sbs+hzJ4SNe2patU4aopfqzHTStBVpzidBZ2sRjq6Je81LPv83IV8IpHIKTUTcJKdxjU7MU77hIPfV6/YlT/lsFOCFzUHby/V4Uq3bLOcdPKuld2mfaPORbe6WTS4OMpg8hhqpSdTLi6ilriJcmnnXRPKz1L+1UrQ1WI0lZi3fO6lAx9OeOilk/PrUO5+Ndub3HTK+d8V9CB5XE56uLZOBMoSdJa+jppyUoxlflzoZ3B5BmX2TIPLPsrFnys0L/g7hpkmxXBVs4JRppY6Lf6UUuh8d8J2pi7L3FOqIS/kl1qGlCaFzI3X4Olbu62hLxUKX+xYdMnQZykXa3LjCvO/zu0/Rj3eMrjpz0wMfP60aVfPOOzIk17zI+jZp0gmnjG4veyihf83odCNDyHke3UQ9NuOOv6cw6iZmlSZTxX2c7NY/+4up/yt+qyjW2JVmHzZeo10dC5WxZj4sZ9yEkumv1gL/8oU9LWUydMNM3NaofZG+apuANVr8hlL07+9cHH6mXJFXd0YnLd83dJi9baa7U1+2Qi7D0EPS67JritH0GPJ1HlB3I0l5QHDawaHuvtXjZXBbGfG0vVdxdKtZgWjXKzY2mj6F/buRMo5/foXf1DM7mLnZvYN7kGZfFilHeVynp9e7IXiiyVTYygX74XxYcs1pkz4ncubEPJNQiJ/jfWuuaQ7YRfr4PVgjykuKLU2tKZph6jhYupXZWF/nxDyu7a2tu/kcozH+7c3rPSCLSxC3Nypa3v4Ws8Z5IyEHE2ZKGNNAOn0sCevnsVWBl4ytNr+lSPo6o1Nbl6U+v/kW1/cddZC+6eUlTfPQrdpr5zbu9pzzfJqtjelfPR7HoLul1SThytH0A1uzwjqvhIiIymuD9sgxsyUc8HSZ5cVS7eaFUxnqXvC2G4w+/Vzry1vbu/s2uo8fXgx30udi7HUCWHsd68xTLG8VBq559vb279BCFm11157/euhD77Q1dObyv12+yztE9eqpWY7rpD/mnud1/9jx4796vgJhwypqWTV3OljxkUctcJZBQV+4/DkNUVX4jKsVJmiLqNePka5faLXZxY3H4pt9eTQL73i9nu8Wv6VI+iUiwfC3MjqTKoy/89izIqeY+K1S+bLb3uxq2Z745Vm0OMQ9KDEmjR8rQVdYcqu35xMi6KVqMiTj8Hsu+ryCsxxPkYt+74wdutcvKmevOpdTLLfUYuwLeabbtnPn3+Z95NKId80TduLEGK7a4T39Ao1rvpJasnpUXPgq4WuKXVsRuzheWdFb3ZOOO1i5/CjT3Xaxx/oRCLtjuqB/u2xkazQq/+V2Ks51t2f2t8cpi0bbvgm4G1CSEpNWKNp2slqqtlS6avz6gZk86gE/29pRrC10ld5pTO7d6OuW0H7HAzbYdlPz1gW/FNMvi3V8q8egq58i1nhPpW5eTYnYX83n5G7D0F3SWBbdwL1EHTl9IylG0z1tO1WmIDbP8fjQ//mBa+aFUz1UA9o6xYfDUvqXjbX6rhamS2s/bpl3xjUzra2tq8RQgbGjBmTXU5UvdIN85SVm24Pfzau881lR01cM2XW3c65xm2O6rR29PFTnIlHTcr2rFe963N/hx812Tl44vdeJISs1DTt96qDnXqDMHHixKKdLHPTzv1fZ+LisCzVp5vcuHL/z/UvSPyqs2a0b+31uXGV8381/KuXoF+YGPpmOaMGYlyc48Wymu2NV5pBj+MJPSixJg1fL0HXk+mTgjRWI8Ja9sqOuPfKYtWsYLpp/2qELcGedl/QLfuQehYVnYvLw9pvWPalQW0fXlBl5dixY32NkfcTP02mPDsmquFxUbM/O+5djX3P/3UlVs1Vr//9pFMqzL5/f3XynPlhhuOpSZLE3V7xF/OvdN7JWV7xBj1eDf/qJeizWGZHysXq0vwKv3GJWsJzaGg125ugeeYVHoLuRabFjtdL0ClLH1jGWNjVqhJ5ZUU1K5huyZ+GbRTUddkVs5iYpi9e/0kv+6t5nFriZ2HtjyXlnKC2EUJ21DTtgTFjxpSccctv3NRMhb4p0Sv4luTHN60/WY1eCMNTt8T9Xv62sn/1EvThTwgPhMmrzdekPG+UqtneeJWRoMch6EGJNWn4egl6D7fbm1LQFw/+KHyjkHP3b4m/qE6FYRbsKKeoGUl7WVj7jT45N2jaEydO/DdCyKOqt3vQa73CG6a4LKwPNCmpV7xBj89eKE4JbQe37/NKr5X9q5egq089w7MxhrsBY/Zsr/yCoHuRwfGaE6iXoMeS2Zmwcns8B6lodXtC7+GpfSgTL4dvyHNEffPr+hfU05rB5KVRlj7OSNjfnBHv36FaBaHWgr552Br5IBKJnFApnxpF8KjV2oJeDf/qKehq2FvYeqtD0CtVfRFPNQlA0LcR2A1qWVEv5qpDV8fip+5V4+HDNg4lrvsHZfZKg4ubKZO02+ofW6wDoJedXsfrIOjT1XjxSCRiedkU9DgEfZsyu7UsVvANBAR9K+ePoqCXaKe2ljkunKB1GOGrRIBaIhYk43LDhhmH7rrRrE/oyv6OJevOraKgj6golIt3h4dHLTSYfbDLL+y21oIeiUT+Swm6pmkPh7U5/zoI+lahya2P2f8bXNA7Eo/vTFnoiY1CjUNX5Ue9cscTunxzm/ISrFNvftu0ZT+/jmK/TgS6Nr/63hAmoz+qgj7bfPJz1JIDYZiVec3bejL1p2ifPKPUghFexanGgv5xQsjjwzO6bXCHrnnZ5vc4BL15BV0ttGOwzC2Uh3rDBUH3W0nywoXpFBekrcpLDrv1JNBtCbWCWODFGT6qgq7ySucDZwUp8BUPa9kru82hyUHLTS0Fffz48bsQQl4dFvR3NU0bE9TeQuEh6M0r6Nm6s3j9JykT14aoExD0QhXCxzEIug9IrRREdfYyuP14kEr2URZ0NVOdYYo7gvCqSlhL3nTRQvs//JbFWgp6e3v7noSQd4YFXb12/75fO4uFg6A3t6Bn81bNusgFC1gnIOjFKkaRcxD0InBa9VRHQu7cxdJ309JrcGe/nXykBX3UqFFqbnWDiXq8et/y7Uo1iLMXpNYe9sBLbX7KZS0FPRKJ7EMIedcVdEJIzI+NpcJA0FtA0IczWee2QbkotmhPblmHoJeqHB7nIegeYFr9sJqBrdtcs0L3IeofdUFXZcHoSxOdi/UBnzRyG6mK/B8z7efmzN9YdEGRrL01HIc+LOjvuYIeiUSurET9gaC3jqCr8qAnxTkGs/0sngJBD1mBIOghwbXCZWTlyu2mLl23tNRawhD0zbl92AObDpgz3x6xXnk9BF7nqYy+xHvInbK2lk/ohJDdCSFvuYKuFkCpRP2AoLeWoKsy0Xn50yfoSbmpRL2BoIesQBD0kOBa6TLKU5cUq2AQ9K25fdHCtXuo+bmL8arFOZ3L3xdb/KTGgr4jIWS9K+iEkNu2Egv/HwS99QRdlYbOy9cdaJj2uiL1BIIestpA0EOCa7XLosyeOTwWeptXwxD0kbmtxrrGuJyVna+9QuM7izRu2+THlrDMPnOkZVv3ainoKtVIJPKIK+iRSOSxrZaE/w+C3pqCrkpErHftvkUWUYGgh6w2EPSQ4FrxMmqtnUy5/Y8tgjEsVhD0wrlNFw18Q7fE8u6E/VLMDDXe1lusfd0oyCfjHtPH1lrQ1Wt2V9AJIWLSpEn/Upia/6MQ9NYVdFUK6ML0VygXD+a3N5QLCLr/ajIiZBhBHxEBdlqLQMeCzHdmLUxtyP2uDkEvnscnrHhu9E+uf/qi7oS9ppy1mAs0bCUF3+DpEwtZV2tBj0Qip+YI+uDo0aO3L2RXkGMQ9NYWdFUW1IInMcu+Pa/sQ9CDVJScsBD0HBj4dzOBs69b0zb9iiHhijoE3V/JmDp15XadLHMw5XY8yrNj11UHui0L06glOF2meQ1YSeH2DG+lbipkXa0Ffbhj3BvDoj5UibXIIeitL+iq7Ko1DKiVyZ2ABoJeqFL7OAZB9wHpoxjk9GvW79WdEH9TQgJBD1cC1Hj/KBffMvrs42LcnjPlqsG7uvpkxlOcfb1m36aRX1NoethaC7oiFIlE7nQFXS2nGo7a1qsg6Nvk9dYbvwafy31rLvr7T3XwnHnZU2zz2gnyoWIdPovFiLnc03tRFmwu92I8ca6FCMztHdzF4PZ9BreNsG418+IsYX0udt3U5fYXdDZwPLXsGykXfsbkbm3ECwv+P6OWGJufZp0E/dxhQV8HQXfFuPHXQ6/Gamv55dHvPjXteZTb9ylh9ntNbjgIOgQ9tzzg/zwC+uL+nWLJ9L55h33v9nC7Pfe1c8Cn07qth+7bwTICZufWt8SdAZnkCbzt6IsHj843ox6C3t7e/nlCyGuEkGda6Rt6t9na66FXy7/8Mul3n5pyXNilhJWgUy7+GLZOfRSXT/WbLwgHAqPUUqBhK5ca1qI6zXhhVOcoF6FWkFPXFVsP3SvNSh+Px52P68w2y2DkRLk8I9+ucgQ9xuWc/Pj87mua9itCyIsTJ078hN9rvMJRM3V5WC66JXWveIMeP/+qdSeHHcWgW+J+r/Ra3T8vv6t5PPs9vnDP+bwbYfcNysgtBL2auYO4m56AweUPwzbKan3w6JKhz3pBqLegh/3OV8gfasn8nr6+GiDFNsZFZ36clNlXhOZuynh+fH73NU07hBDSX4lha92m3RfWhxgX3X5tLhVun/5/TJp76YDv/Miz+Q9e8be6f15+V/N4PP7KZ6llh193gQnPdQjq3d744YZOcX4oIUxoAoYlQzfKlIu/xJKrPu2VeD0rWIyL+TpP3zVjWf8OXvYFOU7N9OQ8IfAtIIYluvLT0pndGzY+ndu/yo/P7756Mp9w4BELZlirk8fc/cIhfq8rFO6s64cuVSMDgvqhc+H0mGJ5oTjDHJu+RM4JPTqBif/2SrPV/fPyu5rHL5n/j29Tbr8XtMy44XUuLvCyr57tjZdN+cch6PlEsF8xAl29g7tQLsrp1f3HePx+z1e3dalgajlVnrKUaKhGoCuZ+p/okrW7lguNJuXpRuFObyUFzWCpbQTdYKLHbaRCbJ+fYfV/KaxP0+f9pZeaA86s3tS7MdO+iy7KfDfMN9GDH3zRmDM/3JOxwUTG6LO/ENYH9zp98SOfNEx7VQiGw/kmf+HGlb9tdf/y/a3Fvp60l4bPK+HMvlSc5mVnXdobL2M8jkPQPcDgcHkE1LdhyuwbyqlcMcu+sZgVta5gyieD28sK+LS6Y8na7xSztdS5aVdmbgnzNJq1JSlPz4+/y8ycUsDOkjcHW65hItQCK5TbR1IuRs42yLI3Pw8aXJ6hL17/yXxbvfapKSeV0aHS6UraS73i9nvcsMrs32CJHq+0Wt0/L7+rdTzK0ydSLt7cUoYD3CAPv4H54NAHNx3oZV+Z7c1G1dlPxVGJn5eNEHQvMjgemkDUkhpl4rdhKlbuNTFuF/2WW2YFC9QpTgkRZfZ1ufaN+N8S70QtcVVniBEB5/183bm6JUK9JpwzP+XsLd7Y5maiw5TjKBcfjLAxQAOnrotye1E8vuFTfgsC3dwb/IUSaarZ9Dov5E9/uVS8sWRqDOXi9RLxFb1J6V4wcOWJt3n3w/CyoYen/j3G5aJybiiU3d1W5rteabS6f15+V/p4tHftrj3mqjm6lQ49DHRY0J/a78lXv+Zln9vehPz8ouri88OdeFVH3rC/bBxeNoYR9Nz6Nbw+RVjb3OuK2uhlO44HINDFU/voyfRJlMsfVPynnqRYegrlglFu/6FC46s/iDHxvWIuuhUst0AG+N+3oKuJW3QmbvUVNxOvRfvW/iZmpk+a2/v8Ll6fDFSHuvYHX93T4IN93Qn7HV9xFxDkmClennTjs/vkc5qZGPi8wUUqbLzudQYKq7oAAAwXSURBVAYXj3Zb6cmKdX4aal8Jfre1VouZ6espF++715Xa6pYc7Fz8lBlLrtuzULzq2KQVzvZdi4bWbJ5sZGRv5FLxu+djpnCmL82sOffqoQu6LhvcZZTjfMwrPXU8OxmQJc/XuSjjNftmW1XjWGwkRSv7p/q+xJLie3pSnlDp9ibbjlnpMzd/VpK/LvOzXs4NoX1bPL7S8wbWbW9CCnpOOuHKslum1SJaXmW4XEHPSaNcez1t9LIdxwMQoFzOq2BmlZvZfq7fOItldizmolvBQvrlS9DnJOTOlAdcLpVJ178XKbf/12DpyzoXD8xWvdGjiwao3jdg6lzcRbl4tRyxUn7rTPzVaxIO1bktJBvX/q1bJjKU2TdGuT2PMhFTw3uoZV9hcPEXJeRun4Ig6Q37/ooaYqdzu72Q2HYtziwul5EacpaddlcJrCV+22Om51MmphmWOC3G7FMps6dEk3KezuWK4aeorX4XuIny46NuCSeWkHerzzTFynCr+pcVFi5C36j6YVzxMItsWiyv3PamAQT9HS87G0jQPW30sh3HAxCgXBRd87zilSNkQ7jFDqv0N1y3gm25JliaJQX9uFuf2W32gtSfQsY/UhS2ivzI48Fs3uZandmXehWD4W+K21xTEX/KtDvfBoOJl6Zd9cTe+b5cMj99UNjPEflp1HJfCfqsRHpKvj/5+63qX/cC+fUKvamrTflVb9a4+FZ+/uTuu+0NBN3XGwYIem7hqfT/TSbo7+qWXXLIk1vBQjbURQV90orX95l2ZebvYScUCWlTsMaLyTfi8fReXmVleNasx2tiSxkCr1u202M+fX087mwzokF9stCZuKfRfShgX6bUGyaVb63q37Cgh+qgVoBlsHoRoizGuFjhVY/c4257A0GHoLtlom7bZhJ0ndu/L/WqUoF0K1jIBsBT0Hu43C2WkDIWYgx0SFtCNVhdfallpQoUtdJn1tKmMGlFrdSzFy189itevnSYA0eX2zktjF3lXKPz9IVe/uQfb0X/mkrQLfF2NJE6KD9f8vfd9gaCDkHPLxs1328iQX/bMAeIH0BuBQvZ8HoK+iy2ckfDHLgjZLyhxDloWjEu11/w8ye+WoqTegKkln1f0PhrF97+kC4amlzKD8pE7vKaNWEcloHO5UNe/Rq8/Gw1/5pJ0A3uPVdAbn657Q0EHYKeWy7q8n+zCHqMy1l+AbkVLGTD6ynoKv14/InPdllp1eGr4cTD4LbT2fvUj/xyoszeX32jbkRfKLcX+fFDdU6MWWJtY/qwtYxkh/2Ycpwfn3LDtJp/zSLoOhdrY8n0F3Pzwut/t72BoG8t70XqI76hexWkShxvBkHXubjZz6t2l4dbwYoUqmJiXFTQVRoXLl23p26JVXTzxCjF4qrpOd2ye10GfrcdvYNnGpZd1rj0kJwLslE912Nm6s4gE810mXYb5WJTJe2oZFy6Jd/vWZj6id88yQ/XSv41haAz+cr0K1ZNyM8Hr323vYGgQ9C9ykjNjje6oBtc3hakcVfg3AoWslEuKegqjR6e+rJuicr0dK/A077BxGVBbnpyC5iaIjYkq4KiHDau7kTK0a3Be86+7hXPhXdy7c793+DiKMrFy2HTruJ1H3QxMS3X1jD/t4p/DS3oTDgGs1/TzfSxQfLIbW8g6BD0IOWmKmEbVtDV029SXlNsERYvIG4FC9lI+xJ0lbaaE7ybydvUUKSQaVXkuiiX87xY+D0etURHT6/9br0aJTUe/JzlQ7dOvN8JLOauj5SlD4yZtgw9VW4FbqxGlAMmXtMX22e59pW7bQX/GlnQo0mZ9jOKJj8f3famXnUnp8x5vs7GOPT8XGvR/cYUdPmCYa6ZERa5W8FyCnoQ4fQt6Mo+NbObbtlzKRdvh0wviG35YZ9Vk6CE5ZR/3Zk3rDtp5pL0U3UQxPdnzU/xqctXbpdvU9D9aVdl9j7nF0/dXe+RCLplr+npfe7woPaXCt/s/jWmoNvO7Pmp2w98+NWvl+Jf6Lzb3jSyoHdamb0pk40wXNDzpqMQWxwLSKCRBF3n4p9RJm+YeeXANwK6MSK4W8FCCmwgQXcTntubOZJa4m8h08wX6lL77xhMXBc1B0r2Znft87v9yS+e2W3G4syNeoDpWsP7bDs6l/ashfIEv/b5CUdWOtvNifd3xkx7Q0+vSqOmb1Be19nAYn1x/05+bA0Tppn9ayRBN1jKoTy1elbvc2eG/Vyl8s9tbxpa0FlmdzUDZfi6WrE6BEEPU+n9XtMIgq5zsamHpa6JJtIlx3z68cutYCELbyhBV3apb/2GJXVqyadDpl1KyB3DFHfoSbHNoit+uAQJM/PyzHd1S95bLT8oT23qTqw3/fYkDmK7G/a8Kwf3+OHN65M9valSC8OU5O6DwzvUEjd1+hxa6dpYzrYZ/WsUQY8mxZ8vuOKZafF4/w7l5IG6Vl+8fifVf6MBBN1znvRmsLHcfMD16jswsy/10VhVosHLxqFz26HcVnN8p6KmuCPGU+epCVsqmRlu4Q3p18vdlgz16s31Ibveu5rXnItVsxYqf8u7u9W5EDoXyymXE8p5knDt87udFO/ffsayDUcbidRvKBPPlOsHZdLJLgzDUvys5c+XxdivDypcPC53U8MeqSUfVQtYlO3HcH5mn/wtsSbK5RKdDewXxKZKhm0m/4bncve9WE+l8krNxW9Y8hFqiSRdJCcE7WhbLL/UCnwGE7dQJl6hPLs6opqr3u/vLcrFxjJWWfO1klkz2FiMMc75JKAWo6Bc2kp8KvWjXD5pWOIJagnVgD6gm+J3lMlrdWvgp7PNZ0+jbN2BatUvnyYGDuYW3pgpHjCYfZ/fnwqvM3GLWnYxcKIFLlAd+o685/mjqCU45WI15dkKX2qI2NsGEy+qxVWyq9RZA99Xne8KRF/TQ+r1vvpeT5m4ivLsuG/Vo3zE0q7qCUW94qZbOwl+SJl4jVpigFryZxde1v+DauZ7KSDqZiiWEOMpk9SwxJ1RJp/dZo324jdfb1HLVk/7f+5m8tLZpjxarbhXKt1anW8G/+ii9FeGP02trlR748ZDLfkYTdoPUy7+YHD7Zp3ZiymT0w0mj+m2MtusB1DpfFEPJuphQK2kV+ufm24pn5rBxlI+4HwJAtmnPrV8ZKV/JdL9KJ3ONrbJ1J4xJo83LNGlW/ZPaVIkKZeLdCZ7dS66DS7PMPrEoWoykUZmo3wx+tbuoRrKqCXPNVSnQFMmOi7PLJr8n+t5d2Jgrs7tCw1un9jFB/dRHQcb0Z+p8Q2filpirM7kj2hSRimXCWrKpTqTV1NmX2MkM8t0S/ZRU87SuX1Wt5U+qJrfxivNqGH9q3Q748ZXaYCIDwRAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAAARAoIUI/D/OfPWHMuH1FwAAAABJRU5ErkJggg=="/>
               </defs>
            </svg>
            <div style="display: flex; flex-direction: column; justify-content: space-between">
               <p style="color: #F4F4F4; font-size: 14px; align-self: flex-start; margin-bottom: 10px;">
                  Discoodle est un outil pédagogique permettant aux étudiants ou aux professeurs d'université de
                  profiter des avantages de la plateforme Discord et Moodle en une même application.
               </p>
               <a href="" style="color: #8F8F8F; font-size: 10px; align-self: flex-end">En savoir plus...</a>
            </div>
         </div>
      </div>
   </div>
</template>

<script>
import axios from 'axios';
import vueCookie from 'vue-cookie'
import {mapActions} from 'vuex'

export default {
   data() {
      return {
         showLogin: true
      }
   },
   methods: {
      userRegistration() {
         axios.post(`http://localhost:8080/api/registration`, {
            mail: document.querySelector("input[name=mailReg]").value,
            password: document.querySelector("input[name=passwordReg]").value,
            lastName: document.querySelector("input[name=lastname]").value,
            name: document.querySelector("input[name=name]").value,
            username: document.querySelector("input[name=username]").value,
         }).then(response => {
            console.log(response);
            this.showLogin = true;
         }).catch(error => {
            console.log(error.response);
         });
      },
      login() {
         axios.post(`http://localhost:8080/api/registration/login`, {
            username: document.querySelector("input[name=userlog]").value,
            password: document.querySelector("input[name=passwordlog]").value,
         }).then(response => {
            if (response.data) {
               if (document.querySelector(".login > div > label > input[type=checkbox]").checked)
                  vueCookie.set("username", document.querySelector("input[name=userlog]").value, {expires: '1Y'});

               axios.get(`http://localhost:8080/api/users/${document.querySelector("input[name=userlog]").value}`).then(response => {
                  this.setUser(response.data);
                  this.$emit("logSuccess")
               }).catch(error => {
                  console.log(error);
               })
            }
         }).catch(error => {
            console.log(error.response);
         });
      },
      actionInputLogin(event) {
         event.keyCode === 13
            ? this.login()
            : ""
      },
      actionInputRegister(event) {
         event.keyCode === 13
               ? this.userRegistration()
               : ""
      },
      ...mapActions(['setUser'])
   }
}
</script>

<style scoped>
.Authentification {
   width: 100%;
   height: 100%;

   overflow-y: auto;
   overflow-x: hidden;

   display: flex;
   align-items: center;
   justify-content: center;
}

.Authentification::before {
   content: "";
   width: calc(100% - 270px);
   height: 100%;
   position: absolute;
   background-image: url("../assets/connecte-image.png");
   background-size: cover;
   top: 0;
   right: 0;
   filter: opacity(0.2) grayscale(0.5);
}

.content {
   position: relative;
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: center;

   padding: 40px;
   width: 60%;
   height: 50%;
   background-color: #454150;
   border-radius: 8px;
   z-index: 20;
}

.content > div {
   height: 100%;
   width: 50%;
}

.login {
   display: flex;
   align-items: center;
   justify-content: center;
   width: calc(50% - 1px) !important;
   border-right: #909090 1px solid;

   animation: appear-opacity 0.7s ease-in-out;
   padding: 30px 30px 30px 0;
}

.login > div > *,
.register > * {
   margin-bottom: 10px;
}

.register {
   display: flex;
   align-items: center;
   justify-content: center;
   flex-direction: column;
   width: calc(50% - 1px) !important;
   border-right: #909090 1px solid;

   animation: appear-opacity 0.7s ease-in-out;
   padding: 30px 30px 30px 0;
}

.about {
   display: flex;
   flex-direction: column;
   align-items: center;
   justify-content: center;
   padding: 30px 0 30px 30px;
}


@keyframes appear-opacity {
   from {
      opacity: 0;
   }
   to {
      opacity: 1;
   }
}


/* CSS INPUT STYLES */

input {
   width: 100%;
   padding-left: 15px;
   height: 30px;
   border-radius: 999px;
   color: #8F8F8F;
   font-weight: 600;
   outline: none;
   border: none;
   font-size: 12px;
}

label {
   color: #F4F4F4;
   display: flex;
   flex-direction: row;
   align-items: center;
   justify-content: flex-start;
   width: 100%;
   font-size: 12px;
   font-weight: 600;
}

input[type="checkbox"] {
   width: 14px;
   height: 14px;
   background-color: #909090;
   border-radius: 3px;
   border: none;
   outline: none;
   margin-right: 5px;
}

.submit {
   height: 35px;
   width: 150px;
   background-color: #E85C5C;
   font-size: 15px;
   font-weight: 600;
   border: none;
   outline: none;
   border-radius: 999px;
   color: #F4F4F4;
   cursor: pointer;
}

</style>
