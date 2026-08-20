package movieBookingapplication.AuthenticationPackege;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import movieBookingapplication.Entity.User;
import movieBookingapplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtAuthenticationfilter extends OncePerRequestFilter {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader=request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        if(authHeader==null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Extract the Jwt Token from the Header
        jwtToken=authHeader.substring(7);
        username=jwtService.extractUsername(jwtToken);

        //check if We have a username aur no authentication yet
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            User userDetails=userRepository.findByUsername(username).
                    orElseThrow(()->new RuntimeException("User Not Found"));

            if(jwtService.validateToken(jwtToken, userDetails)){

                //create authentication with user roles
                List<SimpleGrantedAuthority> authorities=userDetails.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());


                UsernamePasswordAuthenticationToken authenticationToken=
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                authorities
                        );

                //set Authentication Token
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //update the security context
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }

        }

       filterChain.doFilter(request, response);
    }




}
