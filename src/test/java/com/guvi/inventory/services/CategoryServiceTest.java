package com.guvi.inventory.services;
import com.guvi.inventory.DTO.CategoryRequest;
import com.guvi.inventory.DTO.CategoryResponse;
import com.guvi.inventory.model.Category;
import com.guvi.inventory.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock package com.guvi.inventory.services;
import com.guvi.inventory.DTO.CategoryRequest;
import com.guvi.inventory.DTO.CategoryResponse;
import com.guvi.inventory.model.Caelimport com.guvi.inventory.DTO.Categ eimport com.guvi.inventory.DTO.CategoryResponsNaimport com.guvi.inventory.model.Category;
imporiimport com.guvi.inventory.repository.Catroimport org.junit.jupiter.api.BeforeEach;
import org.junreimport org.junit.jupiter.api.DisplayNam  import org.junit.jupiter.api.Nested;
impreimport org.junit.jupiter.api.Test;

 import org.junit.jupiter.api.exteucimport org.mockito.InjectMocks;
import org.mockit.eimport org.mockito.Mock;
imporenimport org.mockito.juni  import java.util.List;
import java.util.Optional;(eimport java.util.Opti  import static org.junit.jseimport static org.mockito.ArgumentMatchers.any;
quimport static org.mockito.ArgumentMatchers.arg  import static org.mockito.Mockito.*;
@ExtendWith(M  @ExtendWith(s("Electronics", responseclass CategoryServiceTest {
    @Me(    @Mock package com.guvi  import com.guvi.inventory.DTO.CategoryRequest;
import com.guvi.inventory.DTO.CategoryResponsthimport com.guvi.inventory.model.Caelimport comouimporiimport com.guvi.inventory.repository.Catroimport org.junit.jupiter.api.BeforeEach;
import org.junreimport org.junit.jupiter.api.DisplayNam  import org.jun.cimport org.junreimport org.junit.jupiter.api.DisplayNam  import org.junit.jupiter.a"Electimpreimport org.junit.jupiter.api.Test;

 import org.junit.jupiter.api.exteucimport org.mock  
 import org.junit.jupiter.api.exteuciegoimport org.mockit.eimport org.mockito.Mock;
imporenimport org.mocke imporenimport org.mockito.juni  import javUpimport java.util.Optional;(eimport java.util.Opti  imyRquimport static org.mockito.ArgumentMatchers.arg  import static org.mockito.Mockito.*;
@ExtendWith(M  @ExtendWith(s("Electrohe@ExtendWith(M  @ExtendWith(s("Electronics", responseclass CategoryServiceTest {
    @v.    @Me(    @Mock package com.guvi  import com.guvi.inventory.DTO.CategoryRequeCimport com.guvi.inventory.DTO.CategoryResponsthimport com.guvi.inventory.model.Ca("import org.junreimport org.junit.jupiter.api.DisplayNam  import org.jun.cimport org.junreimport org.junit.jupiter.api.DisplayNam  import org.junit.jupiter.a"Electimpreimport org.juniul
 import org.junit.jupiter.api.exteucimport org.mock  
 import org.junit.jupiter.api.exteuciegoimport org.mockit.eimport org.mockito.Mock;
imporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.jupiter.api.exteuciegoimport org.mteimporenimport org.mocke imporenimport org.mockito.juni  import javUpimport java.utal@ExtendWith(M  @ExtendWith(s("Electrohe@ExtendWith(M  @ExtendWith(s("Electronics", responseclass CategoryServiceTest {
    @v.    @Me(    @Mock package com.guvi  import com.guvi.inventory.DTO.CategoryRequeCimWh    @v.    @Me(    @Mock package com.guvi  import com.guvi.inventory.DTO.CategoryRequeCimport com.guvi.inventory.DTO.ow import org.junit.jupiter.api.exteucimport org.mock  
 import org.junit.jupiter.api.exteuciegoimport org.mockit.eimport org.mockito.Mock;
imporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.jupiter.api.exteuciegoimport org.mteimporenimport org.mocke imporenimport org.mockito.juni  import javUpimport java.utal@ExtendWith(Mhe import org.junit.jupiter.api.exteuciegoimport org.mtuimporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.      @v.    @Me(    @Mock package com.guvi  import com.guvi.inventory.DTO.CategoryRequeCimWh    @v.    @Me(    @Mock package com.guvi  import com.guvi.inventory.DTO.CategoryRequeCimport com.guvi.inventory.DTO.ow import org.junit.jupiter.api.exteucimport org.mock  
 import org.junit.jupiter.api.exteuciegoimport org.mocket import org.junit.jupiter.api.exteuciegoimport org.mockit.eimport org.mockito.Mock;
imporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.jupiter.api.exteuciegoimport org.mteimporenimport org.mocke imporenimport org.mockito.juni  impoesimporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.() import org.junit.jupiter.api.exteuciegoimport org.mocket import org.junit.jupiter.api.exteuciegoimport org.mockit.eimport org.mockito.Mock;
imporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.jupiter.api.exteuciegoimport org.mteimporenimport org.mocke imporenimport org.mockito.juni  impoesimporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.() import org.junit.jupiter.api.exteuciegoimport org.mocket import org.junit.jupiter.api.exteuciegoimport org.mockit.eimport org.mockito.Mock;
imporenimport org.mocke imporenimport org.mockito.juni  impor    importknimporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.jupiter.api.exteuciegoimport org.mteimporenimport org.moc  imporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.jupiter.api.exteuciegoimport org.mteimporenimport org.mocke imporenimport org.mockito.juni  impoesimporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.() import org.junit.jupiter.api.exteuciegoimport org.mocket import org.junit.jupiter.api.exteuciegoimport org.mockit.eimport org.mockito.Mockreimporenimport org.mocke imporenimport org.mockito.juni  impor    importknimporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.jupiter.api.exteuciegoimport org.mteimporenimport org.moc  imporenimport org.mocke imporenimport org.mockito.juni  impor    import org.junit.jupiter.api.exteuciegoimport org.mteimporenimport org.mocke imporenimport org.mockcat > /tmp/CategoryServiceTest.java << 'JAVAEOF'
package com.guvi.inventory.services;
import com.guvi.inventory.DTO.CategoryRequest;
import com.guvi.inventory.DTO.CategoryResponse;
import com.guvi.inventory.model.Category;
import com.guvi.inventory.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock CategoryRepository categoryRepository;
    @InjectMocks CategoryService categoryService;
    private Category electronics;
    @BeforeEach
    void setUp() {
        electronics = new Category();
  package com.guvi.inventory.services;
import coms.import com.guvi.inventory.DTO.Categctimport com.guvi.inventory.DTO.CategoryRespons  import com.guvi.inventory.model.Category;
impostimport com.guvi.inventory.repository.CatnNimport org.junit.jupiter.api.BeforeEach;
import org.junyNimport org.junit.jupiter.api.Test;
impo  import org.junit.jupiter.api.extee(import org.mockito.InjectMocks;
import org.mockitReimport org.mockito.Mock;
imporatimport org.mockito.junieqimport java.util.List;
import java.util.Optional;s(import java.util.Opti  import static org.junit.js"import static org.mockito.ArgumentMatchers.any;
);import static org.mockito.ArgumentMatchers.arg);import static org.mockito.Mockito.*;
@ExtendWith(Mow@ExtendWith(MockitoExtension.class)atclass CategoryServiceTest {
    @Mtr    @Mock CategoryReposito
     @InjectMocks CategoryService categoryServicas    private Category electronics;
    @BeforeEacat    @BeforeEach
    void setUp()ro    void setUp))        electronica  package com.guvi.inventory.service))import coms.import com.guvi.inventorygoimpostimport com.guvi.inventory.repository.CatnNimport org.junit.jupiter.api.BeforeEach;
import org.junyNimport org.junit.jupiter.api.Teatimport org.junyNimport org.junit.jupiter.api.Test;
impo  import org.junit.jupiter.api.etoimpo  import org.junit.jupiter.api.extee(import o(0import org.mockitReimport org.mockito.Mock;
imporatimport org.mockito.j Cimporatimport org.mockito.junieqimport j    import java.util.Optional;s(import java.util.Opti  imTe);import static org.mockito.ArgumentMatchers.arg);import static org.mockito.Mockito.*;
@ExtendWith(Mow@ExtendWith(MockitoExttr@ExtendWith(Mow@ExtendWith(MockitoExtension.class)atclass CategoryServiceTest {
    @nt    @Mtr    @Mock CategoryReposito
     @InjectMocks CategoryService categorySgo     @InjectMocks CategoryService")    @BeforeEacat    @BeforeEach
    void setUp()ro    void setUp))        electronto    void setUp()ro    void set))import org.junyNimport org.junit.jupiter.api.Teatimport org.junyNimport org.junit.jupiter.api.Test;
impo  import org.junit.jupiter.api.etoimpo  import org.junit.jupiter.api.extee(import o(0import org.mockitReimport or) impo  import org.junit.jupiter.api.etoimpo  import org.junit.jupiter.api.extee(import o(0import orpdimporatimport org.mockito.j Cimporatimport org.mockito.junieqimport j    import java.util.Optional;s(import java.util.Opti  imTe);i
 @ExtendWith(Mow@ExtendWith(MockitoExttr@ExtendWith(Mow@ExtendWith(MockitoExtension.class)atclass CategoryServiceTest {
    @nt    @Mtr    @Mock CategoryReposito
     @InjectMocks CategoryService categorySgo     @I,     @nt    @Mtr    @Mock CategoryReposito
     @InjectMocks CategoryService categorySgo     @InjectMocks CategoryServ.t     @InjectMocks CategoryService catego      void setUp()ro    void setUp))        electronto    void setUp()ro    void set))import org.junyNimport orgatimpo  import org.junit.jupiter.api.etoimpo  import org.junit.jupiter.api.extee(import o(0import org.mockitReimport or) impo  import org.junit.jupiter.api.etoimpo  import org.junit.jufi @ExtendWith(Mow@ExtendWith(MockitoExttr@ExtendWith(Mow@ExtendWith(MockitoExtension.class)atclass CategoryServiceTest {
    @nt    @Mtr    @Mock CategoryReposito
     @InjectMocks CategoryService categorySgo     @I,     @nt    @Mtr    @Mock CategoryReposito
     @InjectMocks CategoryService categorySgo     @InjectMocks CategoryServ.t     @InjectMo).    @nt    @Mtr    @Mock CategoryReposito
     @InjectMocks CategoryService categorySgo     @I,     @nt    @Mtr    @Mod(     @InjectMocks CategoryService catego       @InjectMocks CategoryService categorySgo     @InjectMocks CategoryServ.t     @InjectMocks      @nt    @Mtr    @Mock CategoryReposito
     @InjectMocks CategoryService categorySgo     @I,     @nt    @Mtr    @Mock CategoryReposito
     @InjectMocks CategoryService categorySgo     @InjectMocks CategoryServ.t     @InjectMo).    @nt    @Mtr    @Mock CategoryReposito
     @InjectMocks CategoryService categorySgo     @I,     @nt    @Mtr    @Mod(     @InjectMocks CategoryService catego       @InjectMocks CategoryService categorySgo     @InjectMocks CategoryServ.t     @InjectMocks      @nt    @Mtr    @Mock CategoryReposito
  uv     @InjectMocks CategoryService categont     @InjectMocks CategoryService categorySgo     @InjectMocks CategoryServ.t    python3 -c "
content = '''package com.guvi.inventory.services;
import com.guvi.inventory.DTO.CategoryRequest;
import com.guvi.inventory.DTO.CategoryResponse;
import com.guvi.inventory.model.Category;
import com.guvi.inventory.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock CategoryRepository categoryRepository;
    @InjectMocks CategoryService categoryService;
    private Category cat;
    @BeforeEach
    void setUp() {
        cat = new Category();
        cat.setId(1L);
        cat.setNamcontent = 'niimport com.guvi.inventory.DTO.CategoryRequest;
i  import com.guvi.inventory.DTO.CategoryResponsoiimport com.guvi.inventory.model.Category;
impo) import com.guvi.inventory.repository.CatByimport org.junit.jupiter.api.BeforeEach;
import org.juncaimport org.junit.jupiter.api.Test;
impon(import org.junit.jgoryResponse r = import org.mockito.InjectMocks;
import org.mockitt(import org.mockito.Mock;
impor  import org.mockito.juni.gimport java.util.List;
import java.util.Optional;  import java.util.Optiitimport static org.junit.j  import static org.mockito.ArgumentMatchers.any;
liimport static org.mockito.ArgumentMatchers.argxiimport static org.mockito.Mockito.*;
@ExtendWith(M  @ExtendWith(MockitoExtension.class)ticlass CategoryServiceTest {
    @Mte    @Mock CategoryReposito(n    @InjectMocks CategoryService categoryServic      private Category cat;
    @BeforeEach
    voy(    @BeforeEach
    voidvo    void setUpor        cat = newld        cat.setId(1L);
     ep        cat.setNamcon.ti  import com.guvi.inventory.DTO.CategoryResponsoiimport com.guvi.inventory.Gaimpo) import com.guvi.inventory.repository.CatByimport org.junit.jupiter.api.BeforeEach;
i> import org.juncaimport org.junit.jupiter.api.Test;
impon(import org.junit.jgoryResponsew impon(import org.junit.jgoryResponse r = import ossimport org.mockitt(import org.mockito.Mock;
impor  import org.mockito.jegimpor  import org.mockito.juni.gimport javatimport java.util.Optional;  import java.util.Optiitimcaliimport static org.mockito.ArgumentMatchers.argxiimport static org.mockito.Mockito.*;
@ExtendWith(M  @ExtendWith(MockitoExtor@ExtendWith(M  @ExtendWith(MockitoExtension.class)ticlass CategoryServiceTest {
    @ry    @Mte    @Mock CategoryReposito(n    @InjectMocks CategoryService categorySTh    @BeforeEach
    voy(    @BeforeEach
    voidvo    void setUpor        cat = newld        cat.setId(1L);
 rt    voy(    @BAr    voidvo    void set
      ep        cat.setNamcon.ti  import com.guvi.inventory.DTO.Catori> import org.juncaimport org.junit.jupiter.api.Test;
impon(import org.junit.jgoryResponsew impon(import org.junit.jgoryResponse r = import ossimport org.mockitt(import org.mockito.Mock;
impor  igoimpon(import org.junit.jgoryResponsew impon(import oruimpor  import org.mockito.jegimpor  import org.mockito.juni.gimport javatimport java.util.Optional;  import java.util.Optiitimcaliist@ExtendWith(M  @ExtendWith(MockitoExtor@ExtendWith(M  @ExtendWith(MockitoExtension.class)ticlass CategoryServiceTest {
    @ry    @Mte    @Mock CategoryReposito(n    @InjectMocks CategoryService categorySTh    @Bewe    @ry    @Mte    @Mock CategoryReposito(n    @InjectMocks CategoryService categorySTh    @BeforeEach
    voy(    @BTh    voy(    @BeforeEach
    voidvo    void setUpor        cat = newld        cat.setId(1L);
 rt    vo w    voidvo    void set.f rt    voy(    @BAr    voidvo    void set
      ep        cat.setNle      ep        cat.setNamcon.ti  importryimpon(import org.junit.jgoryResponsew impon(import org.junit.jgoryResponse r = import ossimport org.mockitt(import org.moosimpor  igoimpon(import org.junit.jgoryResponsew impon(import oruimpor  import org.mockito.jegimpor  import org.mockito.juni.gimport@T    @ry    @Mte    @Mock CategoryReposito(n    @InjectMocks CategoryService categorySTh    @Bewe    @ry    @Mte    @Mock CategoryReposito(n    @InjectMocks CategoryService categorySTh    @BeforeEach
    voy(    @BTh    voy(    @BeforeEach
    voidvo    void setUpor        cat = newld        cat.setId(1L);
 rt    ca    voy(    @BTh    voy(    @BeforeEach
    voidvo    void setUpor        cat = newld        cat.setId(1L);
 rt    vo w    voidvo    void set.f rt    voy(    @BAr    voidvo    void set
      ep  st()    voidvo    void setUpor        cat dA rt    vo w    voidvo    void set.f rt    voy(    @BAr    voidvo  ry      ep        cat.setNle      ep        cat.setNamcon open('/Users/24481104    voy(    @BTh    voy(    @BeforeEach
    voidvo    void setUpor        cat = newld        cat.setId(1L);
 rt    ca    voy(    @BTh    voy(    @BeforeEach
    voidvo    void setUpor python3 /dev/stdin <<'PYEOF'
path = "/Users/24481104tr/Thiru/Java_Projects/Guvi-mini-projects/inventory-order-management/src/test/java/com/guvi/inventory/services/CategoryServiceTest.java"
lines = [
"package com.guvi.inventory.services;",
"import com.guvi.inventory.DTO.CategoryRequest;",
"import com.guvi.inventory.DTO.CategoryResponse;",
"import com.guvi.inventory.model.Category;",
"import com.guvi.inventory.repository.CategoryRepository;",
"import org.junit.jupiter.api.BeforeEach;",
"import org.junit.jupiter.api.Test;",
"import org.junit.jupiter.api.extension.ExtendWith;",
"import org.mockito.InjectMocks;",
"import org.mockito.Mock;",
"import org.mockito.junit.jupiter.MockitoExtension;",
"import java.util.List;",
"import java.util.Optional;",
"import static org.junit.jupiter.api.Assertions.*;",
"import static org.mockito.ArgumentMatchers.any;",
"import static org.mockito.ArgumentMatchers.argThat;",
"import static org.mockito.Mockito.*;",
"@ExtendWith(MockitoExtension.class)",
"class CategoryServiceTest {",
"path = "/Users/24481104tr/Ty lines = [
"package com.guvi.inventory.services;",
"import com.guvi.inventory.DTO.CategoryRequest;",
"import com.guvi.inventory.DTO.CategoryResponse;",
"importat"package","import com.guvi.inventory.DTO.Categor"E"import com.guvi.inventory.DTO.CategoryResponse;es"import com.guvi.inventory.model.Category;",
"impvo"import com.guvi.inventory.repository.Categqu"import org.junit.jupiter.api.BeforeEach;",
"import org.juec"import org.junit.jupiter.api.Test;",
"impwh"import org.junit.jupiter.api.extenshe"import org.mockito.InjectMocks;",
"import org.mockiry"import org.mockito.Mock;",
"impory"import org.mockito.junit.d\"import java.util.List;",
"import java.util.Optionalru"import java.util.Option  "import static org.junit.jup)."import static org.mockito.ArgumentMatchers.any;",
or"import static org.mockito.ArgumentMatchers.argTh(c"import static org.mockito.Mockito.*;",
"@ExtendWith(Re"@ExtendWith(MockitoExtension.class)",le"class CategoryServiceTest {",
"path   "path = "/Users/24481104tr/Tyat"package com.guvi.inventory.services;"on"import com.guvi.inventory.DTO.Categorgo"import com.guvi.inventory.DTO.CategoryResponse;  "importat"package","import com.guvi.inventory.DTO{""impvo"import com.guvi.inventory.repository.Categqu"import org.junit.jupiter.api.BeforeEach;",
"import org.juec"import org.junit.jupiter.api.Test;",
"imfa"import org.juec"import org.junit.jupiter.api.Test;",
"impwh"import org.junit.jupiter.api.ext,
"impwh"import org.junit.jupiter.api.extenshe"import pd"import org.mockiry"import org.mockito.Mock;",
"impory"import org.mockito.jun,
"impory"import org.mockito.junit.d\"import jaeN"import java.util.Optionalru"import java.util.Option  "impoenor"import static org.mockito.ArgumentMatchers.argTh(c"import static org.mockito.Mockito.*;",
"@ExtendWith(Re"@ExtendWith(MockitoExtener"@ExtendWith(Re"@ExtendWith(MockitoExtension.class)",le"class CategoryServiceTest {",
"pathgo"path   "path = "/Users/24481104tr/Tyat"package com.guvi.inventory.services;"on"impoy_"import org.juec"import org.junit.jupiter.api.Test;",
"imfa"import org.juec"import org.junit.jupiter.api.Test;",
"impwh"import org.junit.jupiter.api.ext,
"impwh"import org.junit.jupiter.api.extenshe"import pd"import org.mockiry"import org.mockito.Mock;",
"impory"import org.mockito.jun,
"impory"import org.mockitonN"imfa"import org.juec"import org.junit.jupiter.api.Tfi"impwh"import org.junit.jupiter.api.ext,
"impwh"import or(c"impwh"import org.junit.jupiter.api.exts\"impory"import org.mockito.jun,
"impory"import org.mockito.junit.d\"import jaeN"import java.util.Opca"impory"import org.mockito.junL,"@ExtendWith(Re"@ExtendWith(MockitoExtener"@ExtendWith(Re"@ExtendWith(MockitoExtension.class)",le"class CategoryServiceTest {",
"pathgo"path   "path = "/Users/24481104tr/Tyat"package com.guvi.inventon("pathgo"path   "path = "/Users/24481104tr/Tyat"package com.guvi.inventory.services;"on"impoy_"import org.juec"import org.junit  "imfa"import org.juec"import org.junit.jupiter.api.Test;",
"impwh"import org.junit.jupiter.api.ext,
"impwh"import org.junit.jupiter.api.extenshe"wh"impwh"import org.junit.jupiter.api.ext,
"impwh"import ormp"impwh"import org.junit.jupiter.api.extrg"impory"import org.mockito.jun,
"impory"import org.mockitonN"imfa"import org.juec"import org.junit.go"impory"import org.mockitonN"i  "impwh"import or(c"impwh"import org.junit.jupiter.api.exts\"impory"import org.mockito.jun,
"impory"import org.mockito.juneg"impory"import org.mockito.junit.d\"import jaeN"import java.util.Opca"impory"import org.m  "pathgo"path   "path = "/Users/24481104tr/Tyat"package com.guvi.inventon("pathgo"path   "path = "/Users/24481104tr/Tyat"package com.guvi.inventory.services;"on"impoy_"import org.juec"import org.junit  "imfa"import org.juec"impoAc"impwh"import org.junit.jupiter.api.ext,
"impwh"import org.junit.jupiter.api.extenshe"wh"impwh"import org.junit.jupiter.api.ext,
"impwh"import ormp"impwh"import org.junit.jupiter.api.extrg"impory"import org.mockito.jun,
"impory"import org.mockitonN"imfa"imposi"impwh"import org.junit.jupiter.api.extLi"impwh"import ormp"impwh"import org.junit.jupiter.api.extrg"impory"import org.mockito.}""impory"import org.mockitonN"imfa"import org.juec"import org.junit.go"impory"import org.m l"impory"import org.mockitocd /Users/24481104tr/Thiru/Java_Projects/Guvi-mini-projects/inventory-order-management && mvn -q test 2>&1 | tail -40
q
exit
ls /Users/24481104tr/Thiru/Java_Projects/Guvi-mini-projects/inventory-order-management/src/test/java/com/guvi/inventory/services/
