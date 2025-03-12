package HugoSantana.Baixatube;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")


public class BaixaTubeController {
    public String baixarVideo(@RequestParam String url, @RequestParam String format){

        try{
            String comando;

            if ("mp3".equals(format)) {
                comando = "yt-dlp -x --audio-format mp3 -o 'downloads/%(title)s.%(ext)s' " + url;
            } else {
                comando = "yt-dlp -f best -o 'downloads/%(title)s.%(ext)s' " + url;
            }
            Process processo = Runtime.getRuntime().exec(comando);
            processo.waitFor();

            File pastaDownloads = new File("downloads");
            File[] arquivos = pastaDownloads.listFiles();
            if (arquivos != null && arquivos.length > 0) {
                return "/api/download" + arquivos[0].getName();
            } else {
                return "erro ao baixar o vídeo";
            }

        } catch ( Exception e ){
        return "erro" + e.getMessage();

        }

    } @GetMapping("/download/{filename}")
    public void downloadArquivo(@PathVariable String filename, HttpServletResponse response) throws IOException{
        File file = new File("downloads/" + filename);
        if (file.exists()){
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            try (FileInputStream fis = new FileInputStream(file);
                 OutputStream os = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
            }
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        
    }





}
