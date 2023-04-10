package xyz.mirai666.uwupaste.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import xyz.mirai666.uwupaste.repository.PasteRepository;
import xyz.mirai666.uwupaste.UwUPasteApplication;
import xyz.mirai666.uwupaste.model.entity.Paste;

import java.awt.*;
import java.time.Instant;
import java.util.List;

public class Util {
    static Logger logger = LoggerFactory.getLogger(UwUPasteApplication.class);
    public static java.util.List<Paste> examplePastes = List.of(
            new Paste("example1", "Rust FFT", "pub fn fast_fourier_transform(input: &[f64], input_permutation: &[usize]) -> Vec<Complex64> {\n" +
                    "    let n = input.len();\n" +
                    "    let mut result = Vec::new();\n" +
                    "    result.reserve_exact(n);\n" +
                    "    for position in input_permutation {\n" +
                    "        result.push(Complex64::new(input[*position], 0.0));\n" +
                    "    }\n" +
                    "    let mut segment_length = 1_usize;\n" +
                    "    while segment_length < n {\n" +
                    "        segment_length <<= 1;\n" +
                    "        let angle: f64 = std::f64::consts::TAU / segment_length as f64;\n" +
                    "        let w_len = Complex64::new(angle.cos(), angle.sin());\n" +
                    "        for segment_start in (0..n).step_by(segment_length) {\n" +
                    "            let mut w = Complex64::new(1.0, 0.0);\n" +
                    "            for position in segment_start..(segment_start + segment_length / 2) {\n" +
                    "                let a = result[position];\n" +
                    "                let b = result[position + segment_length / 2] * w;\n" +
                    "                result[position] = a + b;\n" +
                    "                result[position + segment_length / 2] = a - b;\n" +
                    "                w *= w_len;\n" +
                    "            }\n" +
                    "        }\n" +
                    "    }\n" +
                    "    result\n" +
                    "}", "rust", Instant.now()),
            new Paste("example2", "Java thing (thank you Moulberry#0001)", "// This has bits from lx to ux\n" +
                    "// eg. lx = 1, ux = 3 => 0b00001110\n" +
                    "int mask = ((1 << (ux - lx + 1)) - 1) << lx;\n" +
                    "\n" +
                    "for (int y = ly; y <= uy; y++) {\n" +
                    "    for (int z = 0; z < lz; z++) {\n" +
                    "        removedCount += Integer.bitCount(chunk[y+z*16] & 0xFFFF);\n" +
                    "        chunk[y+z*16] = 0;\n" +
                    "    }\n" +
                    "    for (int z = uz+1; z < 16; z++) {\n" +
                    "        removedCount += Integer.bitCount(chunk[y+z*16] & 0xFFFF);\n" +
                    "        chunk[y+z*16] = 0;\n" +
                    "    }\n" +
                    "    for (int z = lz; z <= uz; z++) {\n" +
                    "        int value = chunk[y+z*16];\n" +
                    "        removedCount += Integer.bitCount((value & ~mask) & 0xFFFF);\n" +
                    "        chunk[y+z*16] = (short)(value & mask);\n" +
                    "    }\n" +
                    "}", "java", Instant.ofEpochSecond(Instant.now().getEpochSecond()-90000)),
            new Paste("example3", "C++ zip function (thank you lumi)", "template<std::ranges::random_access_range ...R>\n" +
                    "requires (std::ranges::sized_range<R> && ...)\n" +
                    "[[nodiscard]] constexpr\n" +
                    "auto zip(R&& ...r)\n" +
                    "-> decltype(auto) {\n" +
                    "    return std::views::iota(std::size_t(0), std::min({std::ranges::size(r) ...}))\n" +
                    "         | std::views::transform(\n" +
                    "           [...r = std::views::all(std::forward<R>(r))] (std::size_t i) mutable\n" +
                    "           -> decltype(auto) {\n" +
                    "               return std::tuple<std::ranges::range_reference_t<R> const& ...>\n" +
                    "                      { std::ranges::begin(r)[i] ... };\n" +
                    "           });\n" +
                    "}", "cpp", Instant.EPOCH),
            new Paste("example4", "Python lambda", "(lambda length: [print(f\"\\a{' '*(10-length)}{(x := ''.join([str(i) for i in range(1, o+1)][::-1]))}{x[::-1].replace('1', '')}\") if (o := int(max(i))) == length else print(''.join(i)) for i in [[' '*(10-i), str(i), ' '*(2*i-2), str(i)] if i != 1 else ['\\t\\t\\a', ' ', str(i)] for i in range(1, length+1)]])(6)\n", "py", Instant.now()),
            new Paste("example5", "C calculator", "#include <stdio.h>\n" +
                    "#include <math.h>\n" +
                    "#define clear 1;if(c>=11){c=0;sscanf(_,\"%lf%c\",&r,&c);while(*++_-c);}\\\n" +
                    "  else if(argc>=4&&!main(4-(*_++=='('),argv))_++;g:c+=\n" +
                    "#define puts(d,e) return 0;}{double a;int b;char c=(argc<4?d)&15;\\\n" +
                    "  b=(*_%__LINE__+7)%9*(3*e>>c&1);c+=\n" +
                    "#define I(d) (r);if(argc<4&&*#d==*_){a=r;r=usage?r*a:r+a;goto g;}c=c\n" +
                    "#define return if(argc==2)printf(\"%f\\n\",r);return argc>=4+\n" +
                    "#define usage main(4-__LINE__/26,argv)\n" +
                    "#define calculator *_*(int)\n" +
                    "#define l (r);r=--b?r:\n" +
                    "#define _ argv[1]\n" +
                    "#define x\n" +
                    "\n" +
                    "double r;\n" +
                    "int main(int argc,char** argv){\n" +
                    "  if(argc<2){\n" +
                    "    puts(\n" +
                    "      usage: calculator 11/26+222/31\n" +
                    "      +~~~~~~~~~~~~~~~~~~~~~~~~calculator-\\\n" +
                    "      !                          7.584,367 )\n" +
                    "      +~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+\n" +
                    "      ! clear ! 0 ||l   -x  l   tan  I (/) |\n" +
                    "      +~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+\n" +
                    "      ! 1 | 2 | 3 ||l  1/x  l   cos  I (*) |\n" +
                    "      +~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+\n" +
                    "      ! 4 | 5 | 6 ||l  exp  l  sqrt  I (+) |\n" +
                    "      +~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~+\n" +
                    "      ! 7 | 8 | 9 ||l  sin  l   log  I (-) |\n" +
                    "      +~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~(0\n" +
                    "    );\n" +
                    "  }\n" +
                    "  return 0;\n" +
                    "}\n", "c", Instant.from(Instant.EPOCH.plusSeconds(86400)))

    );
    public static void saveTestPastes(PasteRepository repo) {
        repo.saveAll(examplePastes);
    }

    public static String color(Color color, String s, Object... param) {
        String ansi = String.format("\033[38;2;%d;%d;%dm", color.getRed(), color.getGreen(), color.getBlue());
        return ansi + String.format(s, param) + "\033[0m";
    }
}
