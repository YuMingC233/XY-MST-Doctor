# This is a configuration file for R8

-verbose
-allowaccessmodification
-repackageclasses

# Note that you cannot just include these flags in your own
# configuration file; if you are including this file, optimization
# will be turned off. You'll need to either edit this file, or
# duplicate the contents of this file and remove the include of this
# file from your project's proguard.config path property.

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# We only need to keep ComposeView
-keep public class androidx.compose.ui.platform.ComposeView {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# For enumeration classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# AndroidX + support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn androidx.**

-keepattributes SourceFile,
                LineNumberTable,
                RuntimeVisibleAnnotations,
                RuntimeVisibleParameterAnnotations,
                RuntimeVisibleTypeAnnotations,
                AnnotationDefault

-renamesourcefileattribute SourceFile

-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Dagger
-dontwarn com.google.errorprone.annotations.*

# Retain the generic signature of retrofit2.Call until added to Retrofit.
# Issue: https://github.com/square/retrofit/issues/3580.
# Pull request: https://github.com/square/retrofit/pull/3579.
-keep,allowobfuscation,allowshrinking class retrofit2.Call

# See https://issuetracker.google.com/issues/265188224
-keep,allowshrinking class * extends androidx.compose.ui.node.ModifierNodeElement {}


-dontwarn com.chenlb.mmseg4j.ComplexSeg
-dontwarn com.chenlb.mmseg4j.Dictionary
-dontwarn com.chenlb.mmseg4j.MMSeg
-dontwarn com.chenlb.mmseg4j.Seg
-dontwarn com.github.houbb.pinyin.constant.enums.PinyinStyleEnum
-dontwarn com.github.promeg.pinyinhelper.Pinyin$Config
-dontwarn com.github.promeg.pinyinhelper.Pinyin
-dontwarn com.github.stuxuhai.jpinyin.PinyinFormat
-dontwarn com.googlecode.aviator.AviatorEvaluator
-dontwarn com.googlecode.aviator.AviatorEvaluatorInstance
-dontwarn com.hankcs.hanlp.HanLP
-dontwarn com.hankcs.hanlp.seg.Segment
-dontwarn com.huaban.analysis.jieba.JiebaSegmenter$SegMode
-dontwarn com.huaban.analysis.jieba.JiebaSegmenter
-dontwarn com.jfirer.jfireel.expression.Expression
-dontwarn com.mayabot.nlp.segment.Lexer
-dontwarn com.mayabot.nlp.segment.Lexers
-dontwarn com.ql.util.express.ExpressRunner
-dontwarn com.rnkrsoft.bopomofo4j.Bopomofo4j
-dontwarn io.github.logtube.Logtube
-dontwarn io.github.logtube.core.IEventLogger
-dontwarn java.beans.Transient
-dontwarn net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
-dontwarn net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
-dontwarn net.sourceforge.pinyin4j.format.HanyuPinyinToneType
-dontwarn net.sourceforge.pinyin4j.format.HanyuPinyinVCharType
-dontwarn org.ansj.splitWord.Analysis
-dontwarn org.ansj.splitWord.analysis.ToAnalysis
-dontwarn org.apache.commons.jexl3.JexlBuilder
-dontwarn org.apache.commons.jexl3.JexlEngine
-dontwarn org.apache.log4j.Logger
-dontwarn org.apache.logging.log4j.LogManager
-dontwarn org.apache.logging.log4j.Logger
-dontwarn org.apache.lucene.analysis.Analyzer
-dontwarn org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer
-dontwarn org.apdplat.word.segmentation.Segmentation
-dontwarn org.apdplat.word.segmentation.SegmentationAlgorithm
-dontwarn org.apdplat.word.segmentation.SegmentationFactory
-dontwarn org.jboss.logging.Logger
-dontwarn org.lionsoul.jcseg.ISegment$Type
-dontwarn org.lionsoul.jcseg.ISegment
-dontwarn org.lionsoul.jcseg.dic.ADictionary
-dontwarn org.lionsoul.jcseg.dic.DictionaryFactory
-dontwarn org.lionsoul.jcseg.fi.SegmenterFunction
-dontwarn org.lionsoul.jcseg.segmenter.SegmenterConfig
-dontwarn org.mozilla.javascript.Context
-dontwarn org.mvel2.MVEL
-dontwarn org.pmw.tinylog.Level
-dontwarn org.pmw.tinylog.Logger
-dontwarn org.slf4j.ILoggerFactory
-dontwarn org.slf4j.Logger
-dontwarn org.slf4j.LoggerFactory
-dontwarn org.slf4j.helpers.NOPLoggerFactory
-dontwarn org.slf4j.spi.LocationAwareLogger
-dontwarn org.springframework.expression.ExpressionParser
-dontwarn org.springframework.expression.spel.standard.SpelExpressionParser
-dontwarn org.tinylog.Level
-dontwarn org.tinylog.Logger
-dontwarn org.tinylog.configuration.Configuration
-dontwarn org.tinylog.format.AdvancedMessageFormatter
-dontwarn org.tinylog.format.MessageFormatter
-dontwarn org.tinylog.provider.LoggingProvider
-dontwarn org.tinylog.provider.ProviderRegistry